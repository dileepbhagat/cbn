package com.dor.cbn.service;

import javax.mail.Session;

import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.OTPResponseDTO;
import com.dor.cbn.model.AdminEntity;

public interface AdminService {
	public CommonResponseDTO loginGenerateOTP(String loginId, String password, Boolean flag) throws Exception;
	public CommonResponseDTO userLogin(String loginId, String password, String otp, String ipAddress) throws Exception;
	public CommonResponseDTO verifyUser(String username, String otp) throws Exception;
	public CommonResponseDTO setPassword(String key, String password) throws Exception;
	public String getAdminMobNo(String loginId) throws Exception;
	public String getAdminOTP(String loginId) throws Exception;
	public OTPResponseDTO createUser(AdminEntity adminEntity) throws Exception;
	public Boolean sendMail(String email, String loginId, Session session) throws Exception;
	public Boolean updateAdminEntity(String loginId,String otp, String ref) throws Exception;
	public CommonResponseDTO verifyMail(String key) throws Exception;
	public CommonResponseDTO userLogout(String loginId) throws Exception;
}
