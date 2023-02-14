package com.dor.cbn.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.CommonRequestDTO;
import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPGenerationRequestDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.dto.OTPValidationRequestDTO;
import com.dor.cbn.dto.RegistrationRequestDTO;
import com.dor.cbn.dto.RegistrationResponseDTO;
import com.dor.cbn.dto.UserLoginRequestDTO;
import com.dor.cbn.model.EmailEntity;
import com.dor.cbn.model.OTPEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.model.TempEntity;
import com.dor.cbn.model.TokenEntity;
import com.dor.cbn.model.UserRoleTokenEntity;
import com.dor.cbn.model.UsersEntity;
import com.dor.cbn.repository.ConfigRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.repository.TokenRepository;
import com.dor.cbn.repository.UserRoleTokenRepository;
import com.dor.cbn.service.CommonService;
import com.dor.cbn.service.JWTUserTokenGenerationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(APIConstants.API_VERSION)
public class CommonController {
	
	@Autowired
    private CommonService commonService;
	
	@Autowired
	private JWTUserTokenGenerationService jwtUserTokenGenerationService;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private UserRoleTokenRepository userRoleTokenRepository;
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private String port;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private Boolean auth;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String enable;
	
	static SecureRandom rnd = new SecureRandom();
	
	private static TempEntity[] tempEntityAL;
	
	// static variables for AES encryption & decryption
	
	private static SecretKey secretKey;
    private Cipher encryptionCipher;
	
	static
	{
		tempEntityAL=new TempEntity[100];
		//System.out.println("Hi");
		try
		{
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(APIConstants.KEY_SIZE);
			secretKey = keyGenerator.generateKey();
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred!");
		}
	}
	
        
	@GetMapping(APIConstants.FETCH_USERS_DETAILS)
    public List<UsersEntity> findAllUsers() throws SQLException {
    	return commonService.getAllUsers();
    }

	/*
	 * @GetMapping("/{module_id}") public ResponseEntity<ModuleEntity>
	 * findModulesById(@PathVariable(value = "module_id") long module_id) {
	 * Optional<ModuleEntity> moduleEntity =
	 * moduleRepository.findByModuleId((int)module_id);
	 * 
	 * if(moduleEntity.isPresent()) { return
	 * ResponseEntity.ok().body(moduleEntity.get()); } else { return
	 * ResponseEntity.notFound().build(); } }
	 */
	
	@GetMapping(APIConstants.FETCH_USERS_DETAILS_ID)
    public UsersEntity findUserById() throws SQLException {
    	return commonService.getUserById();
    }
	
	@PostMapping(APIConstants.GENERATE_OTP)
    public ResponseEntity<OTPResponseDTO> generateOTP(@Validated @RequestBody OTPGenerationRequestDTO requestDTO ) throws SQLException {
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		
		// Checking for already existing OTP for same user
		
		// Validating Request
		
		try
		{
			if(requestDTO.getEmailId().length()==0 || requestDTO.getMobNo().length()!=10)
			{
				responseDTO.setMsg("OTP generation failed, please enter valid email id or mobile no");
				responseDTO.setStatus(false);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("OTP generation failed, please enter valid email id or mobile no");
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		int toBeDeleteIndex=-1, toBeInsertedIndex=0;
		for(int i=0;i<tempEntityAL.length;i++)
		{
			if(tempEntityAL[i]!=null && tempEntityAL[i].getMobNo().equals(requestDTO.getMobNo()) && tempEntityAL[i].getEmailId().equals(requestDTO.getEmailId()))
			{
				toBeDeleteIndex=i;
				break;
			}
		}
		
		if(toBeDeleteIndex!=-1)
			tempEntityAL[toBeDeleteIndex]=null;
		
		//Finding index where tempEntity will be stored;
		
		for(int i=0;i<tempEntityAL.length;i++)
		{
			if(tempEntityAL[i]==null)
			{
				toBeInsertedIndex=i;
				break;
			}
		}
		
		// Generating OTP & Request Id:
		
		TempEntity tempEntity=new TempEntity();
		String tempOTP=""+getRandomNumber(100000,999999);
		tempEntity.setOTP(tempOTP);
		String tempRef="Ref"+getRandomNumber(100000,999999);
		tempEntity.setRequestId(tempRef);
		tempEntity.setMobNo(requestDTO.getMobNo());
		tempEntity.setEmailId(requestDTO.getEmailId());
		tempEntity.setGenerationTimestamp(LocalDateTime.now());
		tempEntityAL[toBeInsertedIndex]=tempEntity;
		Boolean status=sendOTP(tempOTP, tempRef,"+91"+requestDTO.getMobNo());
		responseDTO.setStatus(status);
		if(status==true)
		{
			responseDTO.setMsg("OTP generated successfully");
			return ResponseEntity.ok(responseDTO);
		}
		else
		{
			responseDTO.setMsg("OTP generation failed");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		}
    	
    }
	
	public Boolean sendOTP(String otp, String ref,String mobNo)
	{
		Twilio.init(APIConstants.ACCOUNT_SID, APIConstants.AUTH_TOKEN);
		String msg="OTP for your mobile verification on CBN is:"+otp+" for request no "+ref+" .The otp expires within 10 mins.";
		try
		{
			Message.creator(new PhoneNumber(mobNo),new PhoneNumber(APIConstants.FROM_NUMBER), msg).create();
		}
		catch(Exception e)
		{
			System.out.println("OTP sent failed!");
			return false;
		}
		return true;
	}
	
	@PostMapping(APIConstants.VALIDATE_OTP)
    public ResponseEntity<OTPResponseDTO> validateOTP(@Validated @RequestBody OTPValidationRequestDTO requestDTO) throws SQLException {
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		OTPEntity otpEntity=new OTPEntity();
		
		//validating request
		try
		{
			if(requestDTO.getEmailId().length()==0 || requestDTO.getMobNo().length()!=10 || requestDTO.getOtp().length()!=6)
			{
				responseDTO.setMsg("OTP validation failed, please enter valid email id or mobile no or otp");
				responseDTO.setStatus(false);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("OTP validation failed, please enter valid email id or mobile no or otp");
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		// validating OTP
		boolean status=false;
		for(int i=0;i<tempEntityAL.length;i++)
		{
			if(tempEntityAL[i]!=null && tempEntityAL[i].getMobNo().equals(requestDTO.getMobNo()) && tempEntityAL[i].getEmailId().equals(requestDTO.getEmailId()) &&  tempEntityAL[i].getOTP().equals(requestDTO.getOtp()))
			{
				status=true;
				otpEntity.setEmailId(tempEntityAL[i].getEmailId());
				otpEntity.setEnabled(false);
				otpEntity.setMobNo(tempEntityAL[i].getMobNo());
				otpEntity.setOtp(tempEntityAL[i].getOTP());
				otpEntity.setRequestId(tempEntityAL[i].getRequestId());
				otpEntity.setTimeOfGeneration(tempEntityAL[i].getGenerationTimestamp());
				tempEntityAL[i]=null;
				break;
			}
		}
		
		responseDTO.setStatus(status);
		if(status==true)
		{
			responseDTO= commonService.saveOTP(otpEntity);
			if(responseDTO.getStatus()==false)
			{
				return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		else
		{
			responseDTO.setMsg("OTP Validation failed, due to incorrect OTP");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		}
		
    	return ResponseEntity.ok(responseDTO);
    }
	
	
	@PostMapping(APIConstants.REGISTER) 
	public ResponseEntity<RegistrationResponseDTO> register(@Validated @RequestBody RegistrationRequestDTO requestDTO) throws Exception {
		RegistrationResponseDTO responseDTO =new RegistrationResponseDTO();
		RegistrationEntity registrationEntity =new RegistrationEntity();
		
		//Setting registrationEntity with requestDTO values
		registrationEntity.setFirmName(requestDTO.getFirmName());
		registrationEntity.setAddressLine1(requestDTO.getAddressLine1());
		registrationEntity.setAddressLine2(requestDTO.getAddressLine2());
		registrationEntity.setAddressLine3(requestDTO.getAddressLine3());
		registrationEntity.setVillageOrCity(requestDTO.getVillageOrCity());
		registrationEntity.setPincode(requestDTO.getPincode());
		registrationEntity.setDistrict(requestDTO.getDistrict());
		registrationEntity.setState(requestDTO.getState());
		registrationEntity.setPanNo(requestDTO.getPanNo());
		registrationEntity.setMobNo(requestDTO.getMobNo());
		registrationEntity.setEmailId(requestDTO.getEmailId());
		registrationEntity.setCreatedOn(new Date());
		registrationEntity.setEnabled(false);;
		
		responseDTO=commonService.registerFirm(registrationEntity);
		if(responseDTO.getStatus()==false)
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		else
		{
			Boolean status=sendMail(registrationEntity.getEmailId(),registrationEntity.getUserId());
			if(status==false)
			{
				responseDTO.setStatus(false);
				responseDTO.setMsg("registration failed");
				return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
			}
			else
			{
				responseDTO.setMsg("registered successfully");
				responseDTO.setStatus(true);
				return ResponseEntity.ok(responseDTO);
			}
			
		}
	 }
	 
	public Boolean sendMail(String email, String registrationId) throws Exception
	{
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", auth);
		prop.put("mail.smtp.starttls.enable", enable);
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		    }
		});
		return commonService.sendMail(email,registrationId,session);
	}
	
	
	@PostMapping(APIConstants.VERIFY_MAIL)  //@RequestParam
	public ResponseEntity<CommonResponseDTO> verifyMail(@RequestParam String key) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.verifyMail(key);
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	
	@PostMapping(APIConstants.SET_PASSWORD)  //@RequestParam
	public ResponseEntity<CommonResponseDTO> setPassword(@RequestParam String key, @RequestParam String password) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.setPassword(key,password);
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(APIConstants.LOGIN_GENERATE_OTP)
	public ResponseEntity<CommonResponseDTO> loginGenerateOTP(@Validated @RequestBody UserLoginRequestDTO requestDTO) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.loginGenerateOTP(requestDTO.getLoginId(),requestDTO.getPassword(),requestDTO.getFlag());
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(APIConstants.USER_LOGIN)
	public ResponseEntity<CommonResponseDTO> userLogin(@Validated @RequestBody UserLoginRequestDTO requestDTO, HttpServletRequest httpServletRequest) throws Exception
	{
		String ipAddress=httpServletRequest.getRemoteAddr();
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.userLogin(requestDTO.getLoginId(),requestDTO.getPassword(),requestDTO.getOtp(),ipAddress);
		if(responseDTO.getStatus()==true)
		{
			String securedPassword=generateSHA256SecuredPassword(requestDTO.getPassword());
			String token=jwtUserTokenGenerationService.generateTokenByUsername(requestDTO.getLoginId(),securedPassword,"user");
			TokenEntity tokenEntity=null;
			tokenEntity=tokenRepository.findByLoginId(requestDTO.getLoginId());
			Long exp=findTokenExpiration(token);
			UserRoleTokenEntity userRoleTokenEntity=  userRoleTokenRepository.findByUserRole("user");
			if(tokenEntity!=null)
			{
				tokenEntity.setTokenExpired(false);
				tokenEntity.setToken(token);
				tokenEntity.setTokenExpTimestamp(exp);
			}
			else
			{
				tokenEntity=new TokenEntity();
				tokenEntity.setTokenExpired(false);
				tokenEntity.setLoginId(requestDTO.getLoginId());
				tokenEntity.setToken(token);
				// finding the token expiration timestamp
				tokenEntity.setTokenExpTimestamp(exp);
			}
			responseDTO.setTokenExpTimestamp(exp);
			responseDTO.setValidity(userRoleTokenEntity.getTokenValidity()*60*1000);
			tokenRepository.save(tokenEntity);
			responseDTO.setToken(token);
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(APIConstants.USER_LOGOUT)
	public ResponseEntity<CommonResponseDTO> userLogout(@Validated @RequestBody UserLoginRequestDTO requestDTO) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.userLogout(requestDTO.getLoginId());
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(APIConstants.VERIFY_USER)
	public ResponseEntity<CommonResponseDTO> verifyUser(@RequestParam String key, @RequestParam String otp) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=commonService.verifyUser(key,otp);
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	public Long findTokenExpiration(String token)
	{
		String[] parts = token.split("\\.");
		try
		{
			JSONObject header = new JSONObject(decode(parts[0]));
			JSONObject payload = new JSONObject(decode(parts[1]));
			String signature = decode(parts[2]);
			return payload.getLong("exp"); 
		}
		catch(Exception e)
		{ }
		return null;
	}
	
	private static String decode(String encodedString) {
	    return new String(Base64.getUrlDecoder().decode(encodedString));
	}
	 
	
	public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
	
	private static String generateSHA256SecuredPassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(APIConstants.SALT.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
}

