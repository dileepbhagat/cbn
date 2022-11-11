package com.dor.cbn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FileResponseDTO {
	
	public FileResponseDTO(String fileName, String fileUrl, String message)
	{
		this.fileName=fileName; this.fileUrl=fileUrl; this.message=message;
	}
	private String fileName;
    private String fileUrl;
    private String message;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
