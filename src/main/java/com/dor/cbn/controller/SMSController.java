package com.dor.cbn.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dor.cbn.constants.APIConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(APIConstants.API_VERSION)
public class SMSController {
	
	private final String ACCOUNT_SID="AC390438102e39035b0af2c86629866cd8";
	private final String AUTH_TOKEN="95096f3462a83c92723564c1e60088c4";
	private final String FROM_NUMBER="+19897472613";
	
	@GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message.creator(new PhoneNumber("+918789109897"),
				new PhoneNumber(FROM_NUMBER), "Hello from Twilio 📞").create();

        	return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}

