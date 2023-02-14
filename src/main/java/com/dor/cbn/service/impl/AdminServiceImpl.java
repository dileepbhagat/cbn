package com.dor.cbn.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.dto.RegistrationResponseDTO;
import com.dor.cbn.model.AdminEntity;
import com.dor.cbn.model.ConfigEntity;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.model.LoginLogEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.model.TokenEntity;
import com.dor.cbn.repository.AdminRepository;
import com.dor.cbn.repository.ConfigRepository;
import com.dor.cbn.repository.LoginLogRepository;
import com.dor.cbn.repository.TokenRepository;
import com.dor.cbn.service.AdminService;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

@Service
public class AdminServiceImpl implements AdminService{
	
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
	
    @Value("${spring.mail.username}") 
    private String sender;
    
    @Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
    
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private LoginLogRepository loginLogRepository;

	@Override
	public CommonResponseDTO loginGenerateOTP(String loginId, String password, Boolean flag) throws Exception {
		// TODO Auto-generated method stub
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		try
		{
			AdminEntity adminEntity =adminRepository.findByLoginId(loginId);
			// Hashing the password to verify the user
			String securedPassword="";
			if(flag==false)
				securedPassword=generateSHA256SecuredPassword(password);
			// Getting mobile no of user... used to send OTP
			if(flag==true || (flag==false && adminEntity.getPassword().equals(securedPassword)))
			{
				String mobileNo="+91"+adminEntity.getMobileNo();
				String tempOTP=""+getRandomNumber(100000,999999);
				String tempRef=""+getRandomNumber(1000,9999);
				adminEntity.setOtp(tempOTP);
				adminEntity.setOtpRequestNo(tempRef);
				adminEntity.setOtpTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				Boolean status=sendOTP(tempOTP,tempRef,mobileNo,flag);
				responseDTO.setStatus(status);
				if(status==true)
				{
					adminRepository.save(adminEntity);
					responseDTO.setMsg("OTP sent successfully!");
					responseDTO.setKey(adminEntity.getOtpRequestNo());
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
	

	@Override
	public CommonResponseDTO userLogin(String loginId, String password, String otp, String ipAddress) throws Exception {
		// TODO Auto-generated method stub
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		try
		{
			AdminEntity adminEntity =adminRepository.findByLoginId(loginId);
			List<LoginLogEntity> loginLogEntities=loginLogRepository.findByLoginId(loginId);
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
			if(!adminEntity.getOtp().equals(otp))
			{
				responseDTO.setMsg("Invalid OTP, please enter request no "+adminEntity.getOtpRequestNo());
				responseDTO.setStatus(false);
				return responseDTO;
			}
			Long miliSeconds=System.currentTimeMillis()-adminEntity.getOtpTimestamp().getTime();
			Double otpTime= miliSeconds/(1000.0*60.0);
			ConfigEntity configEntity= configRepository.findByUserType("admin");
			Double sessionExpireTime=configEntity.getSessionOutTimeInMin()*1.0;
			if(otpTime>sessionExpireTime)
			{
				responseDTO.setMsg("OTP expired, Please click to sent again!");
				responseDTO.setStatus(false);
				return responseDTO;
			}
			if(adminEntity.getEmailVerified()==true && adminEntity.getMobileVerified()==true 
				    && adminEntity.getLoginId().equals(loginId) && adminEntity.getPassword().equals(securedPassword))
			{
				adminEntity.setUpdatedOn(new Date());
				Instant instant = Instant.ofEpochMilli(new Date().getTime());
			    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
			    
			 // Creating entity of loginlog class
				LoginLogEntity loginLogEntity =new LoginLogEntity();
				loginLogEntity.setLoginId(loginId);
				loginLogEntity.setLoggedInTimestamp(ldt);
				loginLogEntity.setIpAddress(ipAddress);
				loginLogEntity.setIsSet(false);
				loginLogEntity.setLoggedIn(true);
				
			    adminRepository.save(adminEntity);
			    loginLogRepository.save(loginLogEntity);
				responseDTO.setMsg("Admin is logged in successfully!");
				responseDTO.setStatus(true);
			}
			else
			{
				responseDTO.setMsg("Admin logged in failed, Please either verify email or set password!");
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
			AdminEntity adminEntity=adminRepository.findByLoginId(loginId);
			adminEntity.setUpdatedOn(new Date());
			Instant instant = Instant.ofEpochMilli(new Date().getTime());
		    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		    
			tokenEntity.setTokenExpired(true);
			// updating the loginlog table
			LoginLogEntity loginLogEntity=loginLogRepository.findByLoginIdAndIsSet(loginId, false);
			loginLogEntity.setLoggedOutTimestamp(ldt);
			loginLogEntity.setIsSet(true);
			loginLogEntity.setLoggedIn(false);
			
			adminRepository.save(adminEntity);
			loginLogRepository.save(loginLogEntity);
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
			AdminEntity adminEntity =adminRepository.findByLoginId(username);
			if(adminEntity.getOtp().equals(otp))
			{
				responseDTO.setMsg("OTP Validated!");
				responseDTO.setStatus(true);
				// generating user identity code to set password
				String passwordCode=getRandomStringForVerificationCode(257);
				// encrypting the code
				String encPasswordCode=encrypt(passwordCode);
				// handling the cases of / , + & =
			
				encPasswordCode=replaceBase64Char(encPasswordCode);
				adminEntity.setUpdatedOn(new Date());
				adminEntity.setPasswordCode(passwordCode);
				adminRepository.save(adminEntity);
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
	
	
	@Override
	public CommonResponseDTO setPassword(String key, String password) throws Exception {
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		key=replaceAddedChar(key);
		try
		{
			// Decrypting password code
			key= decrypt(key);
			AdminEntity adminEntity=adminRepository.findByPasswordCode(key);
			if(adminEntity!=null)
			{
				// Hashing the password using SHA-256
				String securedPassword=generateSHA256SecuredPassword(password);
				if(adminEntity.getFirstTimePasswordSet()==false)
				{
					ArrayList<String> lastPasswords = new ArrayList<String>();
					lastPasswords.add(securedPassword);
					adminEntity.setFirstTimePasswordSet(true);
					adminEntity.setLastThreePasswords(lastPasswords);
				}
				else if(adminEntity.getFirstTimePasswordSet()==true)
				{
					ArrayList<String> lastPasswords = new ArrayList<String>();
					if(adminEntity.getLastThreePasswords().size()==1)
					{
						lastPasswords.add(adminEntity.getLastThreePasswords().get(0));
						lastPasswords.add(securedPassword);
					}
					else if(adminEntity.getLastThreePasswords().size()==2)
					{
						lastPasswords.add(adminEntity.getLastThreePasswords().get(0));
						lastPasswords.add(adminEntity.getLastThreePasswords().get(1));
						lastPasswords.add(securedPassword);
					}
					else
					{
						lastPasswords.add(adminEntity.getLastThreePasswords().get(1));
						lastPasswords.add(adminEntity.getLastThreePasswords().get(2));
						lastPasswords.add(securedPassword);
					}
					for(int i=0;i<lastPasswords.size()-1;i++)
					{
						if(lastPasswords.get(i).equals(securedPassword))
						{
							responseDTO.setMsg("Password shouldn't be last three passwords!");
							responseDTO.setStatus(false);
							return responseDTO;
						}
					}
					adminEntity.setLastThreePasswords(lastPasswords);
				}
				
				adminEntity.setUpdatedOn(new Date());
				adminEntity.setPassword(securedPassword);
				adminRepository.save(adminEntity);
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
	
	
	@Override
	public String getAdminMobNo(String loginId) throws Exception {
		// TODO Auto-generated method stub
		try
		{
			AdminEntity adminEntity =adminRepository.findByLoginId(loginId);
			if(adminEntity!=null)
			{
				return adminEntity.getMobileNo();
			}
			else
				return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@Override
	public String getAdminOTP(String loginId) throws Exception {
		try
		{
			AdminEntity adminEntity =adminRepository.findByLoginId(loginId);
			if(adminEntity!=null)
			{
				return adminEntity.getOtp();
			}
			else
				return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@Override
	public Boolean updateAdminEntity(String loginId, String otp, String ref) throws Exception {
		try
		{
			AdminEntity adminEntity =adminRepository.findByLoginId(loginId);
			if(adminEntity!=null)
			{
				adminEntity.setUpdatedOn(new Date());
				adminEntity.setOtp(otp);
				adminEntity.setOtpRequestNo(ref);
				adminRepository.save(adminEntity);
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	@Override
	public OTPResponseDTO createUser(AdminEntity adminEntity) throws Exception {
		// TODO Auto-generated method stub
		OTPResponseDTO responseDTO=new OTPResponseDTO();
		
		// Checking user already exists or not
		AdminEntity temp =adminRepository.findByLoginId(adminEntity.getLoginId());
		if(temp!=null)
		{
			responseDTO.setMsg("user already created!");
			responseDTO.setStatus(false);
			return responseDTO;
		}
		
		adminEntity.setUserId(getRandomStringForUserId(10));
		adminEntity.setVerificationCode(getRandomStringForVerificationCode(257));
		try
		{
			adminRepository.save(adminEntity);
			responseDTO.setStatus(true);
			responseDTO.setMsg("user created successfully");
		}
		catch(Exception e)
		{
			responseDTO.setMsg("user creation failed");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}
	
	@Override
	public Boolean sendMail(String email, String loginId, Session session) throws Exception {
		// TODO Auto-generated method stub
		AdminEntity adminEntity=null;
		Message message = new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress(username,false));
			message.setRecipients(
					Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Verification of Email & Password Set");
			
			//creating verification link
			
			adminEntity=adminRepository.findByLoginId(loginId);
			String verificationCode=adminEntity.getVerificationCode();
			
			// Encrypting verification code
			verificationCode=encrypt(verificationCode);
			verificationCode=replaceBase64Char(verificationCode);
			
			String header="<p>Click below link to verify email address!</p>"+"<br>";
			String htmlContent="<a href="+"http://localhost:4200/user/admin/verify/mail?key="+verificationCode+ " target='_blank'>"+"http://localhost:4200/user/admin/verify/mail?key="+verificationCode+"</a>";
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
			adminRepository.deleteById(adminEntity.getUserId());
			return false;
		}
	}
	
	@Override
	public CommonResponseDTO verifyMail(String key) throws Exception {
		// TODO Auto-generated method stub
		CommonResponseDTO responseDTO=new CommonResponseDTO();
		key=replaceAddedChar(key);
		try
		{
			// Decrypting verification code
			key= decrypt(key);
			
			AdminEntity adminEntity=adminRepository.findByVerificationCode(key);
			if(adminEntity!=null)
			{
				if(adminEntity.getEmailVerified()==false)
				{
					responseDTO.setMsg("Email verified successfully!");
				
					// generating user identity code to set password
					String passwordCode=getRandomStringForVerificationCode(257);
					// encrypting the code
					String encPasswordCode=encrypt(passwordCode);
					// handling the cases of / , + & =
				
					encPasswordCode=replaceBase64Char(encPasswordCode);
				
					adminEntity.setEmailVerified(true);
					adminEntity.setEmailVerifiedOn(new Date());
					adminEntity.setUpdatedOn(new Date());
					adminEntity.setPasswordCode(passwordCode);
					adminRepository.save(adminEntity);
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
	
	public String getRandomStringForUserId(int len)
	{
		 StringBuilder sb = new StringBuilder(len);
		 for(int i = 0; i < len; i++)
		    sb.append(APIConstants.generatorForRegistrationId.charAt(rnd.nextInt(APIConstants.generatorForRegistrationId.length())));
		 return sb.toString();
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
	
	public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
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
	
	public String getRandomStringForVerificationCode(int len)
	{
		 StringBuilder sb = new StringBuilder(len);
		 for(int i = 0; i < len; i++)
		    sb.append(APIConstants.generatorForVerificationCode.charAt(rnd.nextInt(APIConstants.generatorForVerificationCode.length())));
		 return sb.toString();
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
