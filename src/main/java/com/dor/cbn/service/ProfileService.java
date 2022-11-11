package com.dor.cbn.service;

import java.sql.SQLException;

import com.dor.cbn.dto.ProfilePlantRequestDTO;
import com.dor.cbn.dto.ProfileResponseDTO;

public interface ProfileService {
	public ProfileResponseDTO getAllSubstances() throws SQLException;
	public ProfileResponseDTO saveProfilePlantData(ProfilePlantRequestDTO requestDTO) throws SQLException;
	public ProfileResponseDTO updateProfilePlantData(ProfilePlantRequestDTO requestDTO) throws SQLException;
}
