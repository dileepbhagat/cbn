package com.dor.cbn.service.impl;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dor.cbn.dto.AdminPurposeResponseDTO;
import com.dor.cbn.dto.DocumentRequestDTO;
import com.dor.cbn.dto.DocumentResponseDTO;
import com.dor.cbn.dto.DrugTypeRequestDTO;
import com.dor.cbn.dto.DrugTypeResponseDTO;
import com.dor.cbn.dto.PurposeRequestDTO;
import com.dor.cbn.model.DocumentEntity;
import com.dor.cbn.model.DrugTypeEntity;
import com.dor.cbn.model.PurposeEntity;
import com.dor.cbn.repository.DocumentRepository;
import com.dor.cbn.repository.DrugTypeRepository;
import com.dor.cbn.repository.PurposeRepository;
import com.dor.cbn.service.AdminMasterService;

@Service
public class AdminMasterServiceImpl implements AdminMasterService{

	@Autowired
	PurposeRepository purposeRepository;
	
	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	DrugTypeRepository drugTypeRepository;
	
	@Override
	public AdminPurposeResponseDTO getAllPurposes() throws SQLException {
		// TODO Auto-generated method stub
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		try
		{
			List<PurposeEntity> purposeEntities= purposeRepository.findByValid(true);
			purposeEntities.sort(Comparator.comparing(PurposeEntity::getSerialNo));
			responseDTO.setPurposeEntities(purposeEntities);
			responseDTO.setMsg("Fetch Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public AdminPurposeResponseDTO addPurpose(PurposeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		try
		{
			PurposeEntity purposeEntity =new PurposeEntity();
			purposeEntity.setPurposeName(requestDTO.getPurposeName());
			purposeEntity.setPurposeShortCode(requestDTO.getPurposeShortCode());
			purposeEntity.setCreatedOn(requestDTO.getDateOfCreation());
			purposeEntity.setValid(true);
			purposeRepository.save(purposeEntity);
			List<PurposeEntity> purposeEntities= purposeRepository.findByValid(true);
			purposeEntities.sort(Comparator.comparing(PurposeEntity::getSerialNo));
			responseDTO.setPurposeEntities(purposeEntities);
			responseDTO.setMsg("Added Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public AdminPurposeResponseDTO deletePurpose(PurposeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		try
		{
			Optional<PurposeEntity> purposeEntityOptional= purposeRepository.findById(requestDTO.getSerialNo());
			PurposeEntity purposeEntity=purposeEntityOptional.get();
			if(purposeEntity!=null)
			{
				purposeEntity.setValid(false);
				purposeRepository.save(purposeEntity);
			}
			List<PurposeEntity> purposeEntities= purposeRepository.findByValid(true);
			purposeEntities.sort(Comparator.comparing(PurposeEntity::getSerialNo));
			responseDTO.setPurposeEntities(purposeEntities);
			responseDTO.setMsg("Deleted Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public AdminPurposeResponseDTO editPurpose(PurposeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		AdminPurposeResponseDTO responseDTO=new AdminPurposeResponseDTO();
		try
		{
			Optional<PurposeEntity> purposeEntityOptional= purposeRepository.findById(requestDTO.getSerialNo());
			PurposeEntity purposeEntity=purposeEntityOptional.get();
			if(purposeEntity!=null)
			{
				purposeEntity.setPurposeName(requestDTO.getPurposeName());
				purposeEntity.setPurposeShortCode(requestDTO.getPurposeShortCode());
				purposeEntity.setUpdatedOn(new Date());
				purposeRepository.save(purposeEntity);
			}
			List<PurposeEntity> purposeEntities= purposeRepository.findByValid(true);
			purposeEntities.sort(Comparator.comparing(PurposeEntity::getSerialNo));
			responseDTO.setPurposeEntities(purposeEntities);
			responseDTO.setMsg("Updated Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DocumentResponseDTO getAllDocuments() throws SQLException {
		// TODO Auto-generated method stub
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		try
		{
			List<DocumentEntity> documentEntities= documentRepository.findByValid(true);
			documentEntities.sort(Comparator.comparing(DocumentEntity::getSerialNo));
			responseDTO.setDocumentEntities(documentEntities);
			responseDTO.setMsg("Fetch Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DocumentResponseDTO addDocument(DocumentRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		try
		{
			DocumentEntity documentEntity =new DocumentEntity();
			documentEntity.setDocumentName(requestDTO.getDocumentName());
			documentEntity.setDocumentShortCode(requestDTO.getDocumentShortCode());
			documentEntity.setCreatedOn(requestDTO.getDateOfCreation());
			documentEntity.setValid(true);
			documentRepository.save(documentEntity);
			List<DocumentEntity> documentEntities= documentRepository.findByValid(true);
			documentEntities.sort(Comparator.comparing(DocumentEntity::getSerialNo));
			responseDTO.setDocumentEntities(documentEntities);
			responseDTO.setMsg("Added Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DocumentResponseDTO deleteDocument(DocumentRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		try
		{
			Optional<DocumentEntity> documentEntityOptional= documentRepository.findById(requestDTO.getSerialNo());
			DocumentEntity documentEntity=documentEntityOptional.get();
			if(documentEntity!=null)
			{
				documentEntity.setValid(false);
				documentRepository.save(documentEntity);
			}
			List<DocumentEntity> documentEntities= documentRepository.findByValid(true);
			documentEntities.sort(Comparator.comparing(DocumentEntity::getSerialNo));
			responseDTO.setDocumentEntities(documentEntities);
			responseDTO.setMsg("Deleted Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DocumentResponseDTO editDocument(DocumentRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DocumentResponseDTO responseDTO=new DocumentResponseDTO();
		try
		{
			Optional<DocumentEntity> documentEntityOptional= documentRepository.findById(requestDTO.getSerialNo());
			DocumentEntity documentEntity=documentEntityOptional.get();
			if(documentEntity!=null)
			{
				documentEntity.setDocumentName(requestDTO.getDocumentName());
				documentEntity.setDocumentShortCode(requestDTO.getDocumentShortCode());
				documentEntity.setCreatedOn(new Date());
				documentEntity.setValid(true);
				documentRepository.save(documentEntity);
			}
			List<DocumentEntity> documentEntities= documentRepository.findByValid(true);
			documentEntities.sort(Comparator.comparing(DocumentEntity::getSerialNo));
			responseDTO.setDocumentEntities(documentEntities);
			responseDTO.setMsg("Updated Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DrugTypeResponseDTO getAllDrugTypes() throws SQLException {
		// TODO Auto-generated method stub
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		try
		{
			List<DrugTypeEntity> drugTypeEntities= drugTypeRepository.findByValid(true);
			drugTypeEntities.sort(Comparator.comparing(DrugTypeEntity::getSerialNo));
			responseDTO.setDrugTypeEntities(drugTypeEntities);
			responseDTO.setMsg("Fetch Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DrugTypeResponseDTO addDrugType(DrugTypeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		try
		{
			DrugTypeEntity drugTypeEntity =new DrugTypeEntity();
			drugTypeEntity.setDrugTypeName(requestDTO.getDrugTypeName());
			drugTypeEntity.setDrugTypeShortCode(requestDTO.getDrugTypeShortCode());
			drugTypeEntity.setCreatedOn(requestDTO.getDateOfCreation());
			drugTypeEntity.setValid(true);
			drugTypeRepository.save(drugTypeEntity);
			List<DrugTypeEntity> drugTypeEntities= drugTypeRepository.findByValid(true);
			drugTypeEntities.sort(Comparator.comparing(DrugTypeEntity::getSerialNo));
			responseDTO.setDrugTypeEntities(drugTypeEntities);
			responseDTO.setMsg("Added Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DrugTypeResponseDTO deleteDrugType(DrugTypeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		try
		{
			Optional<DrugTypeEntity> drugTypeEntityOptional= drugTypeRepository.findById(requestDTO.getSerialNo());
			DrugTypeEntity drugTypeEntity=drugTypeEntityOptional.get();
			if(drugTypeEntity!=null)
			{
				drugTypeEntity.setValid(false);
				drugTypeRepository.save(drugTypeEntity);
			}
			List<DrugTypeEntity> drugTypeEntities= drugTypeRepository.findByValid(true);
			drugTypeEntities.sort(Comparator.comparing(DrugTypeEntity::getSerialNo));
			responseDTO.setDrugTypeEntities(drugTypeEntities);
			responseDTO.setMsg("Deleted Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public DrugTypeResponseDTO editDrugType(DrugTypeRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		DrugTypeResponseDTO responseDTO=new DrugTypeResponseDTO();
		try
		{
			Optional<DrugTypeEntity> drugTypeEntityOptional= drugTypeRepository.findById(requestDTO.getSerialNo());
			DrugTypeEntity drugTypeEntity=drugTypeEntityOptional.get();
			if(drugTypeEntity!=null)
			{
				drugTypeEntity.setDrugTypeName(requestDTO.getDrugTypeName());
				drugTypeEntity.setDrugTypeShortCode(requestDTO.getDrugTypeShortCode());
				drugTypeEntity.setCreatedOn(new Date());
				drugTypeEntity.setValid(true);
				drugTypeRepository.save(drugTypeEntity);
			}
			List<DrugTypeEntity> drugTypeEntities= drugTypeRepository.findByValid(true);
			drugTypeEntities.sort(Comparator.comparing(DrugTypeEntity::getSerialNo));
			responseDTO.setDrugTypeEntities(drugTypeEntities);
			responseDTO.setMsg("Updated Successfully");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occurred!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

}
