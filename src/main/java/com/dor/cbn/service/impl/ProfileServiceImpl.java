package com.dor.cbn.service.impl;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.dor.cbn.dto.ProfilePlantRequestDTO;
import com.dor.cbn.dto.ProfileResponseDTO;
import com.dor.cbn.dto.UserLoginRequestDTO;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.model.PlantEntity;
import com.dor.cbn.model.PlantSubstanceEntity;
import com.dor.cbn.model.RegistrationEntity;
import com.dor.cbn.model.SubstanceEntity;
import com.dor.cbn.repository.LoginRepository;
import com.dor.cbn.repository.PlantRepository;
import com.dor.cbn.repository.PlantSubstanceRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.repository.SubstanceRepository;
import com.dor.cbn.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private SubstanceRepository substanceRepository;
	
	@Autowired
	private PlantRepository plantRepository;
	
	@Autowired
	private PlantSubstanceRepository plantSubstanceRepository;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Override
	public ProfileResponseDTO getAllSubstances() throws SQLException {
		// TODO Auto-generated method stub
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		try
		{
			List<SubstanceEntity> psychotropicSubstanceEntities= substanceRepository.findByEnabledAndSubstanceType(true,"PSY");
			responseDTO.setPsychotropicSubstancesEntities(psychotropicSubstanceEntities);
			List<SubstanceEntity> narcoticsSubstanceEntities= substanceRepository.findByEnabledAndSubstanceType(true,"NAR");
			responseDTO.setNarcoticsSubstancesEntities(narcoticsSubstanceEntities);
			List<SubstanceEntity> controlledSubstanceEntities= substanceRepository.findByEnabledAndSubstanceType(true,"CTR");
			responseDTO.setControlledSubstancesEntities(controlledSubstanceEntities);
			responseDTO.setMsg("Fetch Successfully!");
			responseDTO.setStatus(true);
		}
		catch(Exception e)
		{
			responseDTO.setMsg("Exception occured!");
			responseDTO.setStatus(false);
		}
		
		return responseDTO;
	}

	@Override
	public ProfileResponseDTO saveProfilePlantData(ProfilePlantRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		String userId="";
		Optional<LoginEntity> loginEntityOptional=loginRepository.findById(requestDTO.getLoginId());
		LoginEntity loginEntity=loginEntityOptional.get();
		if(loginEntity!=null)
			userId=loginEntity.getUserId();
		String plantId="";
		try
		{
			List<PlantEntity> plantEntities=plantRepository.findAll();
			PlantEntity plantEntity=new PlantEntity();
			plantEntity.setCreatedOn(new Date());
			plantEntity.setUpdatedOn(new Date());
			plantEntity.setContrSubPresented(requestDTO.getIsContrSubPresent());
			plantEntity.setNarcoSubPresented(requestDTO.getIsNarcoSubPresent());
			plantEntity.setPsychoSubPresented(requestDTO.getIsPsychoSubPresent());
			plantEntity.setValid(true);
			plantEntity.setLicenseNo(requestDTO.getDrugLicenseNo());
			plantEntity.setLicenseValidityFrom(requestDTO.getValidityFrom());
			plantEntity.setLicenseValidityTo(requestDTO.getValidityTo());
			plantEntity.setPlantAddress(requestDTO.getPlantAddress());
			plantEntity.setPlantName(requestDTO.getPlantName());
			plantEntity.setSerialNo(plantEntities.size()+1);
			plantEntity.setUserId(userId);
			plantEntity.setPlantId(userId+plantEntity.getSerialNo());
			plantId=plantEntity.getPlantId();
			plantRepository.save(plantEntity);
			
			// Saving substances into plant_substance table:
			
			List<PlantSubstanceEntity> plantSubstanceEntities=plantSubstanceRepository.findAll();
			Integer lastSerialNo=plantSubstanceEntities.size();
			
			for(int i=0;i<requestDTO.getPsychotropicSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getPsychotropicSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Psychotropic");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			for(int i=0;i<requestDTO.getNarcoticsSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getNarcoticsSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Narcotics");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			for(int i=0;i<requestDTO.getControlledSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getControlledSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Controlled");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			responseDTO.setMsg("Save successfully!");
			responseDTO.setStatus(true);
			responseDTO.setPlantId(plantId);
			
		}
		catch(Exception e) {
			responseDTO.setMsg("Failed!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public ProfileResponseDTO updateProfilePlantData(ProfilePlantRequestDTO requestDTO) throws SQLException {
		// TODO Auto-generated method stub
		ProfileResponseDTO responseDTO=new ProfileResponseDTO();
		String userId="";
		Optional<LoginEntity> loginEntityOptional=loginRepository.findById(requestDTO.getLoginId());
		LoginEntity loginEntity=loginEntityOptional.get();
		if(loginEntity!=null)
			userId=loginEntity.getUserId();
		String plantId=requestDTO.getPlantId();
		try
		{
			// updating the plant list table
			PlantEntity plantEntity=plantRepository.findByUserIdAndPlantId(userId, plantId);
			plantEntity.setUpdatedOn(new Date());
			plantEntity.setContrSubPresented(requestDTO.getIsContrSubPresent());
			plantEntity.setNarcoSubPresented(requestDTO.getIsNarcoSubPresent());
			plantEntity.setPsychoSubPresented(requestDTO.getIsPsychoSubPresent());
			plantEntity.setValid(true);
			plantEntity.setLicenseNo(requestDTO.getDrugLicenseNo());
			plantEntity.setLicenseValidityFrom(requestDTO.getValidityFrom());
			plantEntity.setLicenseValidityTo(requestDTO.getValidityTo());
			plantEntity.setPlantAddress(requestDTO.getPlantAddress());
			plantEntity.setPlantName(requestDTO.getPlantName());
			plantRepository.save(plantEntity);
			
			// updating substances into plant_substance table:
			
			List<PlantSubstanceEntity> plantSubstanceEntities=plantSubstanceRepository.findByPlantId(plantId);
			for(int i=0;i<plantSubstanceEntities.size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity=plantSubstanceEntities.get(i);
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(false);
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			List<PlantSubstanceEntity> plantSubstanceEntitiesAll=plantSubstanceRepository.findAll();
			Integer lastSerialNo=plantSubstanceEntitiesAll.size();
			
			for(int i=0;i<requestDTO.getPsychotropicSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getPsychotropicSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Psychotropic");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			for(int i=0;i<requestDTO.getNarcoticsSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getNarcoticsSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Narcotics");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			for(int i=0;i<requestDTO.getControlledSubstancesEntities().size();i++)
			{
				PlantSubstanceEntity plantSubstanceEntity =new PlantSubstanceEntity();
				plantSubstanceEntity.setCreatedOn(new Date());
				plantSubstanceEntity.setUpdatedOn(new Date());
				plantSubstanceEntity.setEnabled(true);
				plantSubstanceEntity.setPlantId(plantId);
				plantSubstanceEntity.setSerialNo(++lastSerialNo);
				plantSubstanceEntity.setSubstanceName(requestDTO.getControlledSubstancesEntities().get(i).getSubstanceName());
				plantSubstanceEntity.setSubstanceType("Controlled");
				plantSubstanceRepository.save(plantSubstanceEntity);
			}
			
			responseDTO.setMsg("Save successfully!");
			responseDTO.setStatus(true);
			responseDTO.setPlantId(plantId);
			
		}
		catch(Exception e) {
			responseDTO.setMsg("Failed!");
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

}
