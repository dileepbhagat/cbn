package com.dor.cbn.service;

import java.sql.SQLException;
import java.util.List;

import javax.mail.Session;

import org.springframework.stereotype.Service;

import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.dto.RegistrationResponseDTO;
import com.dor.cbn.model.EmailEntity;
import com.dor.cbn.model.OTPEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.model.UsersEntity;

public interface CommonService {
	
	public List<UsersEntity> getAllUsers() throws SQLException;
	public UsersEntity getUserById() throws SQLException;
	public OTPResponseDTO saveOTP(OTPEntity otpEntity) throws SQLException;
	public RegistrationResponseDTO registerFirm(RegistrationEntity registrationEntity) throws SQLException;
	public Boolean sendMail(String email, String registrationId, Session session) throws Exception;
	public CommonResponseDTO verifyMail(String key) throws Exception;
	public CommonResponseDTO setPassword(String key, String password) throws Exception;
	public CommonResponseDTO loginGenerateOTP(String loginId, String password, Boolean flag) throws Exception;
	public CommonResponseDTO userLogin(String loginId, String password, String otp, String ipAddress) throws Exception;
	public CommonResponseDTO verifyUser(String username, String otp) throws Exception;
	public CommonResponseDTO userLogout(String loginId) throws Exception;
	
}
