package com.dor.cbn.dto;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileRequestDTO {
	
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
