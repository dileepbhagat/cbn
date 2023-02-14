package com.dor.cbn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dor.cbn.config.FileUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class CbnApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbnApplication.class, args);
	}

}
