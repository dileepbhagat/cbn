package com.dor.cbn.service;
import java.io.FileNotFoundException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.dor.cbn.dto.ProfilePlantRequestDTO;
import com.dor.cbn.dto.ProfileRequestDTO;

public interface IFileSytemStorage {
    void init();
    String saveFile(MultipartFile file,ProfileRequestDTO requestDTO, String docShortCode, String plantId, String regSerialNo, String userId, String update);
    Resource loadFile(String fileName) throws FileNotFoundException;
    Boolean saveProfileData(String userId, ProfileRequestDTO requestDTO, String update) throws Exception;
    String saveDrugFile(MultipartFile file,ProfilePlantRequestDTO requestDTO, String docShortCode, String plantId, String regSerialNo, String userId);
}

