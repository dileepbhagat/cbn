package com.dor.cbn.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPGenerationRequestDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.dto.OTPValidationRequestDTO;
import com.dor.cbn.dto.UserLoginRequestDTO;
import com.dor.cbn.model.AdminEntity;
import com.dor.cbn.model.OTPEntity;
import com.dor.cbn.model.TempEntity;
import com.dor.cbn.model.TokenEntity;
import com.dor.cbn.model.UserRoleTokenEntity;
import com.dor.cbn.repository.TokenRepository;
import com.dor.cbn.repository.UserRoleTokenRepository;
import com.dor.cbn.service.AdminService;
import com.dor.cbn.service.JWTUserTokenGenerationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(APIConstants.ADMIN_API_VERSION)
public class AdminController {
	
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
	
	private static TempEntity[] adminTempEntityAL;
	
	static
	{
		adminTempEntityAL=new TempEntity[100];
	}
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private JWTUserTokenGenerationService jwtUserTokenGenerationService;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private UserRoleTokenRepository userRoleTokenRepository;
	
	@PostMapping(APIConstants.LOGIN_GENERATE_OTP)
	public ResponseEntity<CommonResponseDTO> loginGenerateOTP(@Validated @RequestBody UserLoginRequestDTO requestDTO) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=adminService.loginGenerateOTP(requestDTO.getLoginId(),requestDTO.getPassword(),requestDTO.getFlag());
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
		responseDTO=adminService.userLogin(requestDTO.getLoginId(),requestDTO.getPassword(),requestDTO.getOtp(),ipAddress);
		if(responseDTO.getStatus()==true)
		{
			String securedPassword=generateSHA256SecuredPassword(requestDTO.getPassword());
			String token=jwtUserTokenGenerationService.generateTokenByUsername(requestDTO.getLoginId(),securedPassword,"admin");
			TokenEntity tokenEntity=null;
			tokenEntity=tokenRepository.findByLoginId(requestDTO.getLoginId());
			Long exp=findTokenExpiration(token);
			UserRoleTokenEntity userRoleTokenEntity=  userRoleTokenRepository.findByUserRole("admin");
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
				tokenEntity.setTokenExpTimestamp(exp);
			}
			responseDTO.setTokenExpTimestamp(exp);
			tokenRepository.save(tokenEntity);
			responseDTO.setValidity(userRoleTokenEntity.getTokenValidity()*60*1000);
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
		responseDTO=adminService.userLogout(requestDTO.getLoginId());
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
		responseDTO=adminService.verifyUser(key,otp);
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
		responseDTO=adminService.setPassword(key,password);
		if(responseDTO.getStatus()==true)
		{
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(APIConstants.GENERATE_OTP)
    public ResponseEntity<OTPResponseDTO> generateOTP(@Validated @RequestBody OTPGenerationRequestDTO requestDTO ) throws SQLException {
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		
		// Checking for already existing OTP for same user
		
		// Validating Request
		
		try
		{
			if(requestDTO.getEmailId().length()==0 || requestDTO.getMobNo().length()!=10 || requestDTO.getAdminUser().length()==0)
			{
				responseDTO.setMsg("OTP generation failed, please enter valid email id or mobile no or admin");
				responseDTO.setStatus(false);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("OTP generation failed, please enter valid email id or mobile no or admin");
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		int toBeDeleteIndex=-1, toBeInsertedIndex=0;
		for(int i=0;i<adminTempEntityAL.length;i++)
		{
			if(adminTempEntityAL[i]!=null && adminTempEntityAL[i].getMobNo().equals(requestDTO.getMobNo()) && adminTempEntityAL[i].getEmailId().equals(requestDTO.getEmailId()))
			{
				toBeDeleteIndex=i;
				break;
			}
		}
		
		if(toBeDeleteIndex!=-1)
			adminTempEntityAL[toBeDeleteIndex]=null;
		
		//Finding index where tempEntity will be stored;
		
		for(int i=0;i<adminTempEntityAL.length;i++)
		{
			if(adminTempEntityAL[i]==null)
			{
				toBeInsertedIndex=i;
				break;
			}
		}
		
		// Generating OTP & Request Id:
		
		TempEntity tempEntity=new TempEntity();
		
		String tempOTP=""+getRandomNumber(100000,999999);
		tempEntity.setOTP(tempOTP);
		String tempRef="Ref"+getRandomNumber(1000,9999);
		tempEntity.setRequestId(tempRef);
		
		String adminTempOTP=""+getRandomNumber(100000,999999);
		String adminTempRef="Ref"+getRandomNumber(1000,9999);
		
		tempEntity.setMobNo(requestDTO.getMobNo());
		tempEntity.setEmailId(requestDTO.getEmailId());
		tempEntity.setGenerationTimestamp(LocalDateTime.now());
		adminTempEntityAL[toBeInsertedIndex]=tempEntity;
		
		String adminMobNo="";
		try
		{
			adminMobNo=adminService.getAdminMobNo(requestDTO.getAdminUser());
			Boolean status= adminService.updateAdminEntity(requestDTO.getAdminUser(),adminTempOTP,adminTempRef);
			if(status==false)
			{
				responseDTO.setMsg("OTP generation failed");
				return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		catch(Exception e) {}
		
		if(adminMobNo!=null && !"".equals(adminMobNo))
		{
			String msg="OTP for your mobile verification on CBN is:"+tempOTP+" for request no "+tempRef+" ."
					+ "The otp expires within 10 mins.";
			String msgAdmin="OTP for mobile verification for user creation on CBN is:"+adminTempOTP+" for request no "+adminTempRef+" ."
					+ "The otp expires within 10 mins.";
			Boolean status=sendOTP(msg,"+91"+requestDTO.getMobNo());
			status=status && sendOTP(msgAdmin,"+91"+adminMobNo);
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
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    	
    }
	
	@PostMapping(APIConstants.VALIDATE_OTP)
    public ResponseEntity<OTPResponseDTO> validateOTP(@Validated @RequestBody OTPValidationRequestDTO requestDTO) throws SQLException {
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		AdminEntity adminEntity=new AdminEntity();
		
		//validating request
		try
		{
			if(requestDTO.getEmailId().length()==0 || requestDTO.getMobNo().length()!=10 || requestDTO.getOtp().length()!=6 || requestDTO.getAdminOtp().length()!=6 || requestDTO.getAdminUser().length()==0)
			{
				responseDTO.setMsg("OTP validation failed, please enter valid userotp or adminotp");
				responseDTO.setStatus(false);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("OTP validation failed, please enter valid userotp or adminotp");
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		String adminOTP="";
		try
		{
			adminOTP=adminService.getAdminOTP(requestDTO.getAdminUser());
		}
		catch(Exception e) {}
		
		// validating OTP
		try {
		boolean status=false;
		AdminEntity adminEntityUser=null;
		if(adminOTP!=null && !"".equals(adminOTP) && adminOTP.equals(requestDTO.getAdminOtp()))
		{
			for(int i=0;i<adminTempEntityAL.length;i++)
			{
				if(adminTempEntityAL[i]!=null && adminTempEntityAL[i].getMobNo().equals(requestDTO.getMobNo()) && adminTempEntityAL[i].getEmailId().equals(requestDTO.getEmailId()) &&  adminTempEntityAL[i].getOTP().equals(requestDTO.getOtp()))
				{
					status=true;
					adminEntityUser=new AdminEntity();
					adminEntityUser.setCreatedOn(new Date());
					adminEntityUser.setEnabled(false);
					adminEntityUser.setMobileVerified(true);
					adminEntityUser.setEmailVerified(false);
					adminEntityUser.setLoginId(requestDTO.getEmailId());
					adminEntityUser.setMobileNo(requestDTO.getMobNo());
					adminEntityUser.setOtp(requestDTO.getOtp());
					adminEntityUser.setOtpRequestNo(adminTempEntityAL[i].getRequestId());
					adminTempEntityAL[i]=null;
					break;
				}
			}
			responseDTO.setStatus(status);
			if(status==true)
			{
				responseDTO= adminService.createUser(adminEntityUser);
				if(responseDTO.getStatus()==false)
				{
					return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
				}
				else
				{
					status=sendMail(adminEntityUser.getLoginId());
					if(status==false)
					{
						responseDTO.setStatus(false);
						responseDTO.setMsg("user creation failed!");
						return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
					}
				}
			}
			else
			{
				responseDTO.setMsg("User OTP Validation failed, due to incorrect OTP");
				return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		else
		{
			responseDTO.setStatus(false);
			responseDTO.setMsg("Admin OTP validation failed!");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		}
		}
		catch(Exception e) {}
		
    	return ResponseEntity.ok(responseDTO);
    }
	
	@PostMapping(APIConstants.VERIFY_MAIL)  //@RequestParam
	public ResponseEntity<CommonResponseDTO> verifyMail(@RequestParam String key) throws Exception
	{
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		responseDTO=adminService.verifyMail(key);
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
	
	public Boolean sendMail(String email) throws Exception
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
		return adminService.sendMail(email,email,session);
	}
	
	public Boolean sendOTP(String msg, String mobNo)
	{
		Twilio.init(APIConstants.ACCOUNT_SID, APIConstants.AUTH_TOKEN);
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
