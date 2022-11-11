package com.dor.cbn.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.FileResponseDTO;
import com.dor.cbn.dto.MultipartFileRequestDTO;
import com.dor.cbn.dto.ProfileRequestDTO;
import com.dor.cbn.dto.UserLoginRequestDTO;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.model.PlantEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.repository.LoginRepository;
import com.dor.cbn.repository.PlantRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.service.IFileSytemStorage;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(APIConstants.API_VERSION)
public class FileController {
	
	@Autowired
    IFileSytemStorage fileSytemStorage;
	
	@Autowired
	PlantRepository plantRepository;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	private static String[] docShortCode= {"CP","PAN","BS","GST","IEC","IMPEXP"};
	
	//@PostMapping("/uploadfile")
    //public ResponseEntity<FileResponseDTO> uploadSingleFile (@RequestParam("file") MultipartFile file) {
      //  String upfile = fileSytemStorage.saveFile(file);

        //String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
          //      .path("/api/download/")
            //    .path(upfile)
              //  .toUriString();
        
       // return ResponseEntity.status(HttpStatus.OK).body(new FileResponseDTO(upfile,fileDownloadUri,"File uploaded with success!"));
    //}
	
	//@PostMapping("/uploadfile",consumes = {"multipart/form-data"})
	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST, consumes = {"multipart/form-data"})
	@ResponseBody
    public ResponseEntity<FileResponseDTO> uploadSingleFile (@RequestPart("file") @Validated MultipartFile file, @RequestPart("requestDTO") @Validated ProfileRequestDTO requestDTO)
    {
        String upfile = fileSytemStorage.saveFile(file,requestDTO,"ram","shyam","ganesh","mahesh","suresh");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upfile)
                .toUriString();
        
        return ResponseEntity.status(HttpStatus.OK).body(new FileResponseDTO(upfile,fileDownloadUri,"File uploaded with success!"));
    }
	
	@PostMapping("/saveAndUploadfiles")
    public ResponseEntity<List<FileResponseDTO>> uploadMultipleFiles (@RequestParam("files") MultipartFile[] files, @RequestPart("requestDTO") @Validated ProfileRequestDTO requestDTO ,@RequestPart("status") @Validated String update) {
		
		List<FileResponseDTO> responseDTO=new ArrayList<FileResponseDTO>();
		// Finding userId from panno
		String userId="";
		Optional<LoginEntity> loginEntityOptional=loginRepository.findById(requestDTO.getPanNo());
		LoginEntity loginEntity=loginEntityOptional.get();
		if(loginEntity!=null)
			userId=loginEntity.getUserId();
		
		String plantId="0";
		String regSerialNo="Reg";
		// Finding plantId using userId
		List<PlantEntity> plantEntityList=plantRepository.findByUserId(userId);
		PlantEntity plantEntity=plantEntityList.get(plantEntityList.size()-1);
		if(plantEntity!=null)
			plantId=plantEntity.getPlantId();
		
		// Finding RegSerialNo
		RegistrationEntity registrationEntity=registrationRepository.findByUserId(userId);
		if(registrationEntity!=null)
			regSerialNo+=registrationEntity.getSerialNo();
		else
			regSerialNo+="0";
		
		try
		{
			for(int i=0;i<files.length;i++)
			{
				String upfile = fileSytemStorage.saveFile(files[i],requestDTO, docShortCode[i],plantId,regSerialNo,userId,update);
				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/download/")
                    .path(upfile)
                    .toUriString();
				responseDTO.add(new FileResponseDTO(upfile,fileDownloadUri,"File uploaded with success!"));
			}
			Boolean status= fileSytemStorage.saveProfileData(userId, requestDTO, update);
			if(status==true)
				return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
			else
				return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
		}
		
    }
	
	@PostMapping("/updateProfileData")
    public ResponseEntity<FileResponseDTO> updateProfileData (@Validated @RequestBody ProfileRequestDTO requestDTO, @RequestParam("file") MultipartFile file) {
        String upfile = fileSytemStorage.saveFile(file,requestDTO,"ram","shyam","ganesh","mahesh","suresh");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upfile)
                .toUriString();
        
        return ResponseEntity.status(HttpStatus.OK).body(new FileResponseDTO(upfile,fileDownloadUri,"File uploaded with success!"));
    }
	
	
}
