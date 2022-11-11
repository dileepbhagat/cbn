package com.dor.cbn.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dor.cbn.config.FileUploadProperties;
import com.dor.cbn.dto.ProfilePlantRequestDTO;
import com.dor.cbn.dto.ProfileRequestDTO;
import com.dor.cbn.model.DocumentStorageEntity;
import com.dor.cbn.model.ProfileEntity;
import com.dor.cbn.repository.DocumentStorageRepository;
import com.dor.cbn.repository.LoginRepository;
import com.dor.cbn.repository.ProfileRepository;
import com.dor.cbn.repository.RegistrationRepository;
import com.dor.cbn.repository.SequenceRepository;
import com.dor.cbn.service.IFileSytemStorage;

@Service
public class FileSystemStorageService implements IFileSytemStorage {
	
	private final Path dirLocation;
	private static String[] months= {"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	private static String SerialNo="1000000";
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	private DocumentStorageRepository documentStorageRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
    @Autowired
    public FileSystemStorageService(FileUploadProperties fileUploadProperties) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	int day= cal.get(Calendar.MONTH);
    	int year=cal.get(Calendar.YEAR);
        this.dirLocation = Paths.get(fileUploadProperties.getLocation()+"/"+year+"/"+months[day])
                                .toAbsolutePath()
                                .normalize();
        int x=5;
    }

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
            Files.createDirectories(this.dirLocation);
        } 
        catch (Exception ex) {
            //throw new FileStorageException("Could not create upload dir!");
        }
	}

	@Override
	public String saveFile(MultipartFile file, ProfileRequestDTO requestDTO, String docShortCode, String plantId, String regSerialNo, String userId, String update) {
		// TODO Auto-generated method stub
		try {
			//Finding next sequence no
			Long sequenceNo=sequenceRepository.getNextSeriesId();
			Calendar cal = Calendar.getInstance();
	    	cal.setTime(new Date());
	    	String date="_"+cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR);
            String fileName = requestDTO.getPanNo()+"_"+plantId+"_"+regSerialNo+"_"+docShortCode+date+"_"+sequenceNo+".pdf";
            Path dfile = this.dirLocation.resolve(fileName);
            File directory = new File(this.dirLocation.toString());
            if (! directory.exists()){
            	Files.createDirectories(Paths.get(this.dirLocation.toUri()));
            }
            Files.copy(file.getInputStream(), dfile,StandardCopyOption.REPLACE_EXISTING);
            
            // Storing document storage info into db
            
            DocumentStorageEntity documentStorageEntity=null;
            if(update.equals("true"))
            	documentStorageEntity= new DocumentStorageEntity();
            else
            {
            	documentStorageEntity= new DocumentStorageEntity();
            	documentStorageEntity.setUserId(userId);
            }
            documentStorageEntity.setFileName(fileName);
            documentStorageEntity.setValid(true);
            documentStorageEntity.setPath(this.dirLocation.toString());
            documentStorageRepository.save(documentStorageEntity);
            
            return fileName;
            
        } catch (Exception e) {
            //throw new FileStorageException("Could not upload file");
        	int x=5;
        	System.out.println(e);
        }
		return null;
	}

	@Override
	public Resource loadFile(String fileName) throws FileNotFoundException {
		// TODO Auto-generated method stub
		try {
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } 
            else {
                throw new FileNotFoundException("Could not find file");
            }
          } 
          catch (MalformedURLException e) {
              throw new FileNotFoundException("Could not download file");
          }           
      }

	@Override
	public Boolean saveProfileData(String userId, ProfileRequestDTO requestDTO, String update) throws Exception {
		// TODO Auto-generated method stub
		
		// Saving profile data into db
        try
        {
        	ProfileEntity profileEntity=null;
        	if(update.equals("true"))
        	{
        		profileEntity=profileRepository.findByUserId(userId);
        	}
        	else
        	{
        		profileEntity=new ProfileEntity();
        		profileEntity.setProfileId("Pro"+userId);
        		profileEntity.setUserId(userId);
        	}
        	profileEntity.setAddress(requestDTO.getAddress());
        	profileEntity.setAmountOfExciseDuties(requestDTO.getAmountOfExciseDuties());
        	profileEntity.setCity(requestDTO.getCity());
        	profileEntity.setCommissionerateCity(requestDTO.getJurisCommissCity());
        	profileEntity.setCommissionerateName(requestDTO.getJurisCommissName());
        	profileEntity.setDesignatedPerson(requestDTO.getDesignatedPerson());
        	profileEntity.setDesignatedPersonName(requestDTO.getDesignatedPersonName());
        	profileEntity.setEntityName(requestDTO.getEntityName());
        	profileEntity.setEntityType(requestDTO.getEntityType());
        	profileEntity.setGstNo(requestDTO.getGstNo());
        	profileEntity.setIecCode(requestDTO.getIecCode());
        	profileEntity.setEnabled(true);
        	profileEntity.setPanNo(requestDTO.getPanNo());
        	profileEntity.setPincode(requestDTO.getPinCode());
        	profileEntity.setState(requestDTO.getState());
        	profileRepository.save(profileEntity);
        	return true;
        }
        catch(Exception e)
        {
        	return false;
        }
	}

	@Override
	public String saveDrugFile(MultipartFile file, ProfilePlantRequestDTO requestDTO, String docShortCode,
			String plantId, String regSerialNo, String userId) {
		// TODO Auto-generated method stub
		try {
			//Finding next sequence no
			Long sequenceNo=sequenceRepository.getNextSeriesId();
			Calendar cal = Calendar.getInstance();
	    	cal.setTime(new Date());
	    	String date="_"+cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR);
            String fileName = userId+"_"+plantId+"_"+regSerialNo+"_"+docShortCode+date+"_"+sequenceNo+".pdf";
            Path dfile = this.dirLocation.resolve(fileName);
            File directory = new File(this.dirLocation.toString());
            if (! directory.exists()){
            	Files.createDirectories(Paths.get(this.dirLocation.toUri()));
            }
            Files.copy(file.getInputStream(), dfile,StandardCopyOption.REPLACE_EXISTING);
            
            // Storing document storage info into db
            
            DocumentStorageEntity documentStorageEntity=null;
            documentStorageEntity= new DocumentStorageEntity();
            documentStorageEntity.setUserId(userId);
            documentStorageEntity.setValid(true);
            documentStorageEntity.setPath(this.dirLocation.toString());
            documentStorageEntity.setFileName(fileName);
            documentStorageRepository.save(documentStorageEntity);
            
            return fileName;
            
        } catch (Exception e) {
            //throw new FileStorageException("Could not upload file");
        	int x=5;
        	System.out.println(e);
        }
		return null;
	}

}
