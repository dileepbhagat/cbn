package com.dor.cbn.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.dto.OTPValidationRequestDTO;
import com.dor.cbn.dto.RegistrationResponseDTO;
import com.dor.cbn.model.ConfigEntity;
import com.dor.cbn.model.EmailEntity;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.model.LoginLogEntity;
import com.dor.cbn.model.OTPEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.model.TempEntity;
import com.dor.cbn.model.TokenEntity;
import com.dor.cbn.model.UsersEntity;
import com.dor.cbn.repository.ConfigRepository;
import com.dor.cbn.repository.LoginLogRepository;
import com.dor.cbn.repository.LoginRepository;
import com.dor.cbn.repository.OTPRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.repository.TokenRepository;
import com.dor.cbn.repository.UsersRepository;
import com.dor.cbn.service.CommonService;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

@Service
public class CommonServiceImpl implements CommonService{

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private OTPRepository otpRepository;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private LoginLogRepository loginLogRepository;
	
	@Autowired 
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ConfigRepository configRepository;
	 
    @Value("${spring.mail.username}") 
    private String sender;
    
    @Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	private static SecretKey secretKey;
    private Cipher encryptionCipher;
    
    static SecureRandom rnd = new SecureRandom();
    
    static
	{
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
	
	@Transactional
	@Override
	public List<UsersEntity> getAllUsers() throws SQLException {
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public UsersEntity getUserById() throws SQLException {
		Optional<UsersEntity> optionalUsersEntity =userRepository.findById(1);
		return optionalUsersEntity.get();
	}

	@Transactional
	@Override
	public OTPResponseDTO saveOTP(OTPEntity otpEntity) throws SQLException {
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		try
		{
			otpRepository.save(otpEntity);
			responseDTO.setMsg("OTP validated & saved successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("OTP validation success but not saved successfully");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public RegistrationResponseDTO registerFirm(RegistrationEntity registrationEntity) throws SQLException {
		RegistrationResponseDTO responseDTO=new RegistrationResponseDTO();
		
		// Checking user already exists or not
		
		RegistrationEntity temp=registrationRepository.findByPanNo(registrationEntity.getPanNo());
		if(temp!=null)
		{
			responseDTO.setMsg("user already registered");
			responseDTO.setStatus(false);
			return responseDTO;
		}
		
		// Finding next sequence no
		
		int nextVal= registrationRepository.findAll().size()+1;
		registrationEntity.setUserId(registrationEntity.getPanNo()+nextVal);
		registrationEntity.setVerificationCode(getRandomStringForVerificationCode(257));
		try
		{
			registrationRepository.save(registrationEntity);
			// Creating entry in Login table corresponding to User_Registration Table
			Boolean status= createEntryForLogin(registrationEntity.getUserId(),registrationEntity.getPanNo());
			responseDTO.setStatus(status);
			if(status==true)
			   responseDTO.setMsg("registered successfully");
			else
			{
				responseDTO.setMsg("registered failed");
				registrationRepository.deleteById(registrationEntity.getUserId());
			}
			
		}
		catch(Exception e)
		{
			responseDTO.setMsg("registration failed");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public Boolean sendMail(String email, String userId, Session session) throws Exception {
		RegistrationEntity registrationEntity=null;
		Message message = new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress(username,false));
			message.setRecipients(
					Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Verification of Email & Password Set");
			
			//creating verification link
			
			registrationEntity=registrationRepository.findByUserId(userId);
			String verificationCode=registrationEntity.getVerificationCode();
			
			// Encrypting verification code
			verificationCode=encrypt(verificationCode);
			verificationCode=replaceBase64Char(verificationCode);
			
			String header="<p>Click below link to verify email address!</p>"+"<br>";
			String htmlContent="<a href="+"http://localhost:4200/verify/mail?key="+verificationCode+ " target='_blank'>"+"http://localhost:4200/verify/mail?key="+verificationCode+"</a>";
			MimeBodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setContent(header+htmlContent, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e);
			registrationRepository.deleteById(registrationEntity.getUserId());
			loginRepository.deleteByUserId(registrationEntity.getUserId());
			return false;
		}
	}
	
	@Override
	public CommonResponseDTO verifyMail(String key) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		key=replaceAddedChar(key);
		try
		{
			// Decrypting verification code
			key= decrypt(key);
			
			RegistrationEntity registrationEntity=registrationRepository.findByVerificationCode(key);
			LoginEntity loginEntity=null;
			if(registrationEntity!=null)
			{
				loginEntity=loginRepository.findByUserId(registrationEntity.getUserId());
				if(loginEntity.getEmailVerified()==false)
				{
					responseDTO.setMsg("Email verified successfully!");
				
					// generating user identity code to set password
					String passwordCode=getRandomStringForVerificationCode(257);
					// encrypting the code
					String encPasswordCode=encrypt(passwordCode);
					// handling the cases of / , + & =
				
					encPasswordCode=replaceBase64Char(encPasswordCode);
				
					loginEntity.setEmailVerified(true);
					loginEntity.setEmailVerifiedOn(new Date());
					loginEntity.setUpdatedOn(new Date());
					registrationEntity.setPasswordCode(passwordCode);
					registrationRepository.save(registrationEntity);
					loginRepository.save(loginEntity);
					responseDTO.setStatus(true);
					responseDTO.setKey(encPasswordCode);
				}
				else
				{
					responseDTO.setMsg("Email already verified!");
					responseDTO.setStatus(false);
				}
			}
			else
			{
				responseDTO.setMsg("Email not verified!");
				responseDTO.setStatus(false);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			responseDTO.setMsg("Email not verified!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}
	
	@Override
	public CommonResponseDTO setPassword(String key, String password) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		key=replaceAddedChar(key);
		
		try
		{
			// Decrypting password code
			key= decrypt(key);
			RegistrationEntity registrationEntity=registrationRepository.findByPasswordCode(key);
			LoginEntity loginEntity=null;
			if(registrationEntity!=null)
			{
				loginEntity=loginRepository.findByUserId(registrationEntity.getUserId());
				// Hashing the password using SHA-256
				String securedPassword=generateSHA256SecuredPassword(password);
				if(loginEntity.getFirstTimePasswordSet()==false)
				{
					ArrayList<String> lastPasswords = new ArrayList<String>();
					lastPasswords.add(securedPassword);
					loginEntity.setFirstTimePasswordSet(true);
					//loginEntity.setLastThreePasswords(lastPasswords);
				}
				else if(loginEntity.getFirstTimePasswordSet()==true)
				{
					ArrayList<String> lastPasswords = new ArrayList<String>();
					/*if(loginEntity.getLastThreePasswords().size()==1)
					{
						lastPasswords.add(loginEntity.getLastThreePasswords().get(0));
						lastPasswords.add(securedPassword);
					}*/
					/*
					 * else if(loginEntity.getLastThreePasswords().size()==2) {
					 * lastPasswords.add(loginEntity.getLastThreePasswords().get(0));
					 * lastPasswords.add(loginEntity.getLastThreePasswords().get(1));
					 * lastPasswords.add(securedPassword); } else {
					 * lastPasswords.add(loginEntity.getLastThreePasswords().get(1));
					 * lastPasswords.add(loginEntity.getLastThreePasswords().get(2));
					 * lastPasswords.add(securedPassword); } for(int
					 * i=0;i<lastPasswords.size()-1;i++) {
					 * if(lastPasswords.get(i).equals(securedPassword)) {
					 * responseDTO.setMsg("Password shouldn't be last three passwords!");
					 * responseDTO.setStatus(false); return responseDTO; } }
					 */
					//loginEntity.setLastThreePasswords(lastPasswords);
				}
				loginEntity.setUpdatedOn(new Date());
				loginEntity.setPassword(securedPassword);
				loginRepository.save(loginEntity);
				responseDTO.setMsg("Password set successfully!");
				responseDTO.setStatus(true);
			}
			else
			{
				responseDTO.setMsg("Password set failed!");
				responseDTO.setStatus(false);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Password set failed!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
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
	
	@Override
	public CommonResponseDTO loginGenerateOTP(String loginId, String password, Boolean flag) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		try
		{
			Optional<LoginEntity> loginEntityOptional=loginRepository.findById(loginId);
			LoginEntity loginEntity=loginEntityOptional.get();
			// Hashing the password to verify the user
			String securedPassword="";
			if(flag==false)
				securedPassword=generateSHA256SecuredPassword(password);
			// Getting mobile no of user... used to send OTP
			if(flag==true || (flag==false && loginEntity.getPassword().equals(securedPassword)))
			{
				RegistrationEntity registrationEntity=registrationRepository.findByUserId(loginEntity.getUserId());
				String mobileNo="+91"+registrationEntity.getMobNo();
				String tempOTP=""+getRandomNumber(100000,999999);
				String tempRef=""+getRandomNumber(1000,9999);
				loginEntity.setOtp(tempOTP);
				loginEntity.setOtpRequestNo(tempRef);
			    loginEntity.setOtpTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				Boolean status=sendOTP(tempOTP,tempRef,mobileNo,flag);
				responseDTO.setStatus(status);
				if(status==true)
				{
					loginRepository.save(loginEntity);
					responseDTO.setMsg("OTP sent successfully!");
					responseDTO.setKey(loginEntity.getOtpRequestNo());
				}
				else
					responseDTO.setMsg("OTP sent failed!");
				
			}
			else
			{
				responseDTO.setMsg("Invalid credentials!");
				responseDTO.setStatus(false);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Invalid credentials!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}
	
	public Boolean sendOTP(String otp, String ref,String mobNo,Boolean flag)
	{
		Twilio.init(APIConstants.ACCOUNT_SID, APIConstants.AUTH_TOKEN);
		String msg="";
		if(flag==true)
			msg="OTP to set passoword of your account on CBN is:"+otp+" for request no Ref"+ref+" .The otp expires within 10 mins.";
		else
			msg="OTP for login into your account on CBN is:"+otp+" for request no Ref"+ref+" .The otp expires within 10 mins.";
		try
		{
			com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber(mobNo),new PhoneNumber(APIConstants.FROM_NUMBER), msg).create();
		}
		catch(Exception e)
		{
			System.out.println("OTP sent failed!");
			return false;
		}
		return true;
	}
	
	@Override
	public CommonResponseDTO userLogin(String loginId, String password, String otp, String ipAddress) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		try
		{
			Optional<LoginEntity> loginEntityOptional=loginRepository.findById(loginId);
			List<LoginLogEntity> loginLogEntities=loginLogRepository.findByLoginId(loginId);
			LoginEntity loginEntity=loginEntityOptional.get();
			for(int i=0;i<loginLogEntities.size();i++)
			{
				if(loginLogEntities.get(i).getLoggedIn()==true)
				{
					responseDTO.setMsg("You're already logged in, please logout!");
					responseDTO.setStatus(false);
					return responseDTO;
				}
			}
			// Hashing the password to verify the user
			String securedPassword=generateSHA256SecuredPassword(password);
			if(!loginEntity.getOtp().equals(otp))
			{
				responseDTO.setMsg("Invalid OTP, please enter request no "+loginEntity.getOtpRequestNo());
				responseDTO.setStatus(false);
				return responseDTO;
			}
			Long miliSeconds=System.currentTimeMillis()-loginEntity.getOtpTimestamp().getTime();
			Double otpTime= miliSeconds/(1000.0*60.0);
			ConfigEntity configEntity= configRepository.findByUserType("user");
			Double sessionExpireTime=configEntity.getSessionOutTimeInMin()*1.0;
			if(otpTime>sessionExpireTime)
			{
				responseDTO.setMsg("OTP expired, Please click to sent again!");
				responseDTO.setStatus(false);
				return responseDTO;
			}
			if(loginEntity.getEmailVerified()==true && loginEntity.getFirstTimePasswordSet()==true 
				    && loginEntity.getLoginId().equals(loginId) && loginEntity.getPassword().equals(securedPassword))
			{
				RegistrationEntity registrationEntity=	registrationRepository.findByUserId(loginEntity.getUserId());
				loginEntity.setUpdatedOn(new Date());
				Instant instant = Instant.ofEpochMilli(new Date().getTime());
			    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
				
				// Creating entity of loginlog class
				LoginLogEntity loginLogEntity =new LoginLogEntity();
				loginLogEntity.setLoginId(loginId);
				loginLogEntity.setLoggedInTimestamp(ldt);
				loginLogEntity.setIpAddress(ipAddress);
				loginLogEntity.setIsSet(false);
				loginLogEntity.setLoggedIn(true);
				
				loginRepository.save(loginEntity);
				loginLogRepository.save(loginLogEntity);
				
				responseDTO.setFirmName(registrationEntity.getFirmName());
				responseDTO.setMsg("User is logged in successfully!");
				responseDTO.setStatus(true);
			}
			else
			{
				responseDTO.setMsg("User logged in failed, Please either verify email or set password!");
				responseDTO.setStatus(false);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Invalid credentials!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}
	
	@Override
	public CommonResponseDTO userLogout(String loginId) throws Exception {
		// TODO Auto-generated method stub
		CommonResponseDTO commonResponseDTO =new CommonResponseDTO();
		try
		{
			TokenEntity tokenEntity=tokenRepository.findByLoginId(loginId);
			LoginEntity loginEntity=loginRepository.findByLoginId(loginId);
			loginEntity.setUpdatedOn(new Date());
			Instant instant = Instant.ofEpochMilli(new Date().getTime());
		    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
			
			tokenEntity.setTokenExpired(true);
			
			// updating the loginlog table
			LoginLogEntity loginLogEntity=loginLogRepository.findByLoginIdAndIsSet(loginId, false);
			loginLogEntity.setLoggedOutTimestamp(ldt);
			loginLogEntity.setIsSet(true);
			loginLogEntity.setLoggedIn(false);
			loginLogRepository.save(loginLogEntity);
			loginRepository.save(loginEntity);
			tokenRepository.save(tokenEntity);
			
			commonResponseDTO.setMsg("Successfully logout!");
			commonResponseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			commonResponseDTO.setMsg("Error while logout!");
			commonResponseDTO.setStatus(false);
		}
		return commonResponseDTO;
	}
	
	
	@Override
	public CommonResponseDTO verifyUser(String username, String otp) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		try
		{
			Optional<LoginEntity> loginEntityOptional=loginRepository.findById(username);
			LoginEntity loginEntity=loginEntityOptional.get();
			if(loginEntity.getOtp().equals(otp))
			{
				RegistrationEntity registrationEntity=	registrationRepository.findByUserId(loginEntity.getUserId());
				responseDTO.setMsg("OTP Validated!");
				responseDTO.setStatus(true);
				// generating user identity code to set password
				String passwordCode=getRandomStringForVerificationCode(257);
				// encrypting the code
				String encPasswordCode=encrypt(passwordCode);
				// handling the cases of / , + & =
			
				encPasswordCode=replaceBase64Char(encPasswordCode);
				loginEntity.setUpdatedOn(new Date());
				registrationEntity.setPasswordCode(passwordCode);
				registrationRepository.save(registrationEntity);
				loginRepository.save(loginEntity);
				responseDTO.setStatus(true);
				responseDTO.setKey(encPasswordCode);
			}
			else
			{
				responseDTO.setMsg("OTP is not correct!");
				responseDTO.setStatus(false);
			}
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Invalid user!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}
	
	public Boolean createEntryForLogin(String userId, String loginId)
	{
		LoginEntity loginEntity=new LoginEntity();
		loginEntity.setUserId(userId);
		loginEntity.setLoginId(loginId);
		loginEntity.setEmailVerified(false);
		loginEntity.setMobileVerified(false);
		loginEntity.setFirstTimePasswordSet(false);
		loginEntity.setCreatedOn(new Date());
		loginEntity.setEnabled(false);
		
		try
		{
			loginRepository.save(loginEntity);
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred!");
			return false;
		}
		
	}
	
	public String getRandomStringForRegistrationId(int len)
	{
		 StringBuilder sb = new StringBuilder(len);
		 for(int i = 0; i < len; i++)
		    sb.append(APIConstants.generatorForRegistrationId.charAt(rnd.nextInt(APIConstants.generatorForRegistrationId.length())));
		 return sb.toString();
	}
	
	public String getRandomStringForVerificationCode(int len)
	{
		 StringBuilder sb = new StringBuilder(len);
		 for(int i = 0; i < len; i++)
		    sb.append(APIConstants.generatorForVerificationCode.charAt(rnd.nextInt(APIConstants.generatorForVerificationCode.length())));
		 return sb.toString();
	}
	
	public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
	
	
	public String encrypt(String data) throws Exception {
        byte[] dataInBytes = data.getBytes();
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = encryptionCipher.doFinal(dataInBytes);
        return encode(encryptedBytes);
    }
	
	public String decrypt(String encryptedData) throws Exception {
        byte[] dataInBytes = decode(encryptedData);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(APIConstants.DATA_LENGTH, encryptionCipher.getIV());
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);
        return new String(decryptedBytes);
    }
	
	private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
    
    private String replaceBase64Char(String code)
    {
    	for(int i=0;i<code.length();i++)
		{
			if(code.charAt(i)=='+')
			{
				code=code.substring(0, i)+'@'+code.substring(i+1, code.length());
			}
			else if(code.charAt(i)=='/')
				code=code.substring(0, i)+'$'+code.substring(i+1, code.length());
			else if(code.charAt(i)=='=')
				code=code.substring(0, i)+'#'+code.substring(i+1, code.length());
		}
    	return code;
    }

    private String replaceAddedChar(String code)
    {
    	for(int i=0;i<code.length();i++)
		{
			if(code.charAt(i)=='@')
			{
				code=code.substring(0, i)+'+'+code.substring(i+1, code.length());
			}
			else if(code.charAt(i)=='$')
				code=code.substring(0, i)+'/'+code.substring(i+1, code.length());
			else if(code.charAt(i)=='#')
				code=code.substring(0, i)+'='+code.substring(i+1, code.length());
		}
    	return code;
    }
}
