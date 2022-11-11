package com.dor.cbn.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dor.cbn.constants.APIConstants;
import com.dor.cbn.dto.AdminPurposeResponseDTO;
import com.dor.cbn.dto.CommonResponseDTO;
import com.dor.cbn.dto.DocumentRequestDTO;
import com.dor.cbn.dto.DocumentResponseDTO;
import com.dor.cbn.dto.DrugTypeRequestDTO;
import com.dor.cbn.dto.DrugTypeResponseDTO;
import com.dor.cbn.dto.PurposeRequestDTO;
import com.dor.cbn.dto.UserLoginRequestDTO;
import com.dor.cbn.model.PurposeEntity;
import com.dor.cbn.model.UsersEntity;
import com.dor.cbn.service.AdminMasterService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(APIConstants.ADMIN_MASTER_API_VERSION)
public class AdminMasterController {
	
	@Autowired
    private AdminMasterService adminMasterService;
	
	@GetMapping(APIConstants.FETCH_PURPOSE_DATA)
    public ResponseEntity<AdminPurposeResponseDTO> fetchPurposeData() throws SQLException {
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		responseDTO= adminMasterService.getAllPurposes();
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.ADD_PURPOSE_DATA)
    public ResponseEntity<AdminPurposeResponseDTO> addPurposeData(@Validated @RequestBody PurposeRequestDTO requestDTO) throws SQLException {
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		responseDTO= adminMasterService.addPurpose(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.DELETE_PURPOSE_DATA)
    public ResponseEntity<AdminPurposeResponseDTO> deletePurposeData(@Validated @RequestBody PurposeRequestDTO requestDTO) throws SQLException {
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		responseDTO= adminMasterService.deletePurpose(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.EDIT_PURPOSE_DATA)
    public ResponseEntity<AdminPurposeResponseDTO> editPurposeData(@Validated @RequestBody PurposeRequestDTO requestDTO) throws SQLException {
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		responseDTO= adminMasterService.editPurpose(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	
	// APIs for document master
	@GetMapping(APIConstants.FETCH_DOCUMENT_LIST)
    public ResponseEntity<DocumentResponseDTO> fetchDocumentList() throws SQLException {
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		responseDTO= adminMasterService.getAllDocuments();
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.ADD_DOCUMENT)
    public ResponseEntity<DocumentResponseDTO> addDocument(@Validated @RequestBody DocumentRequestDTO requestDTO) throws SQLException {
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		responseDTO= adminMasterService.addDocument(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.DELETE_DOCUMENT)
    public ResponseEntity<DocumentResponseDTO> deleteDocument(@Validated @RequestBody DocumentRequestDTO requestDTO) throws SQLException {
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		responseDTO= adminMasterService.deleteDocument(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@PostMapping(APIConstants.EDIT_DOCUMENT)
    public ResponseEntity<DocumentResponseDTO> editDocument(@Validated @RequestBody DocumentRequestDTO requestDTO) throws SQLException {
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		responseDTO= adminMasterService.editDocument(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }
	
	
	
	// APIs for drug type master
	@GetMapping(APIConstants.FETCH_DRUG_TYPE)
	public ResponseEntity<DrugTypeResponseDTO> fetchDrugType() throws SQLException {
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		responseDTO= adminMasterService.getAllDrugTypes();
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
		
	@PostMapping(APIConstants.ADD_DRUG_TYPE)
	public ResponseEntity<DrugTypeResponseDTO> addDrugType(@Validated @RequestBody DrugTypeRequestDTO requestDTO) throws SQLException {
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		responseDTO= adminMasterService.addDrugType(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
		
	@PostMapping(APIConstants.DELETE_DRUG_TYPE)
	public ResponseEntity<DrugTypeResponseDTO> deleteDrugType(@Validated @RequestBody DrugTypeRequestDTO requestDTO) throws SQLException {
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		responseDTO= adminMasterService.deleteDrugType(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
		
	@PostMapping(APIConstants.EDIT_DRUG_TYPE)
	public ResponseEntity<DrugTypeResponseDTO> editDrugType(@Validated @RequestBody DrugTypeRequestDTO requestDTO) throws SQLException {
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		responseDTO= adminMasterService.editDrugType(requestDTO);
		if(responseDTO.getStatus()==true)
			return ResponseEntity.ok(responseDTO);
		else
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	

}
