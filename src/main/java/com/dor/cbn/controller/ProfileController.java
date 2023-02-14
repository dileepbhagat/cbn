package com.dor.cbn.controller;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.FileResponseDTO;
import com.dor.cbn.dto.ProfilePlantRequestDTO;
import com.dor.cbn.dto.ProfileRequestDTO;
import com.dor.cbn.dto.ProfileResponseDTO;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.repository.LoginRepository;
import com.dor.cbn.repository.PlantRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.service.IFileSytemStorage;
import com.dor.cbn.service.ProfileService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(APIConstants.PROFILE_API_VERSION)
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
    IFileSytemStorage fileSytemStorage;
	
	@Autowired
	PlantRepository plantRepository;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@GetMapping(APIConstants.FETCH_SUBSTANCE_DATA)
    public ResponseEntity<ProfileResponseDTO> fetchSubstanceData() throws SQLException {
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		responseDTO= profileService.getAllSubstances();
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	
	@PostMapping(APIConstants.SAVE_PROFILE_PLANT_DATA)
    public ResponseEntity<ProfileResponseDTO> saveProfilePlantData(@RequestPart("file") @Validated MultipartFile file, @RequestPart("requestDTO") @Validated ProfilePlantRequestDTO requestDTO) throws SQLException {
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		responseDTO= profileService.saveProfilePlantData(requestDTO);
		if(responseDTO.getStatus()==true)
		{
			String userId="";
			Optional<LoginEntity> loginEntityOptional=loginRepository.findById(requestDTO.getLoginId());
			LoginEntity loginEntity=loginEntityOptional.get();
			if(loginEntity!=null)
				userId=loginEntity.getUserId();
			String regSerialNo="Reg";
			RegistrationEntity registrationEntity=registrationRepository.findByUserId(userId);
			if(registrationEntity!=null)
				regSerialNo+=registrationEntity.getSerialNo();
			else
				regSerialNo+="0";
			String upfile = fileSytemStorage.saveDrugFile(file,requestDTO, "DL",responseDTO.getPlantId(), regSerialNo,userId);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upfile)
                .toUriString();
			responseDTO.setMsg("Plant details saved, file uplodaded successfully!");
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	
	@PostMapping(APIConstants.UPDATE_PROFILE_PLANT_DATA)
    public ResponseEntity<ProfileResponseDTO> updateProfilePlantData(@RequestPart("file") @Validated MultipartFile file, @RequestPart("requestDTO") @Validated ProfilePlantRequestDTO requestDTO) throws SQLException {
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		responseDTO= profileService.updateProfilePlantData(requestDTO);
		if(responseDTO.getStatus()==true)
		{
			String userId="";
			Optional<LoginEntity> loginEntityOptional=loginRepository.findById(requestDTO.getLoginId());
			LoginEntity loginEntity=loginEntityOptional.get();
			if(loginEntity!=null)
				userId=loginEntity.getUserId();
			String regSerialNo="Reg";
			RegistrationEntity registrationEntity=registrationRepository.findByUserId(userId);
			if(registrationEntity!=null)
				regSerialNo+=registrationEntity.getSerialNo();
			else
				regSerialNo+="0";
			String upfile = fileSytemStorage.saveDrugFile(file,requestDTO, "DL",responseDTO.getPlantId(), regSerialNo,userId);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upfile)
                .toUriString();
			responseDTO.setMsg("Plant details saved, file uplodaded successfully!");
			return ResponseEntity.ok(responseDTO);
		}
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }

}
