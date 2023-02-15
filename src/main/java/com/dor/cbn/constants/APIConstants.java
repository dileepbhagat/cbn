package com.dor.cbn.constants;

public interface APIConstants {

	String API_VERSION="/api/v1";
	String FETCH_USERS_DETAILS="/users/fetch";
	String FETCH_USERS_DETAILS_ID="/users/fetch/id";
	String GENERATE_OTP="/generate/otp";
	String VALIDATE_OTP="/validate/otp";
	String REGISTER="/register";
	String SEND_MAIL="/send/mail";
	String VERIFY_MAIL="/verify/mail";
	String SET_PASSWORD="/set/password";
	String USER_LOGIN="/user/login";
	String USER_LOGOUT="/user/logout";
	String LOGIN_GENERATE_OTP="/login/generate/otp";
	String VERIFY_USER="/verify/user";
	String generatorForRegistrationId = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String generatorForVerificationCode = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	String SALT="[B@17a7cec2";
	int KEY_SIZE=128;
	int DATA_LENGTH=128;
	
	// SMS OTP constants
	String ACCOUNT_SID="AC390438102e39035b0af2c86629866cd8";
	String AUTH_TOKEN="95096f3462a83c92723564c1e60088c4";
	String FROM_NUMBER="+19897472613";
	
	// Admin master API Constants
	String ADMIN_MASTER_API_VERSION="/api/v1/admin/master";
	String FETCH_PURPOSE_DATA="/fetch/purpose/data";
	String ADD_PURPOSE_DATA="/add/purpose/data";
	String DELETE_PURPOSE_DATA="/delete/purpose/data";
	String EDIT_PURPOSE_DATA="/edit/purpose/data";
	String FETCH_DOCUMENT_LIST="/fetch/document/list";
	String ADD_DOCUMENT="/add/document";
	String DELETE_DOCUMENT="/delete/document";
	String EDIT_DOCUMENT="/edit/document";
	String FETCH_DRUG_TYPE="/fetch/drug/type";
	String ADD_DRUG_TYPE="/add/drug/type";
	String DELETE_DRUG_TYPE="/delete/drug/type";
	String EDIT_DRUG_TYPE="/edit/drug/type";
	
	
	// Admin API Constants
	String ADMIN_API_VERSION="/api/v1/admin";
	
	// Profile Constants
	String PROFILE_API_VERSION="/api/v1/profile";
	String FETCH_SUBSTANCE_DATA="/fetch/substance/data";
	String SAVE_PROFILE_PLANT_DATA="/save/plant/data";
	String UPDATE_PROFILE_PLANT_DATA="/update/plant/data";
	
	
	String DEMO_VARIABLE="DEMO_VARIABLE";
	
	
}
