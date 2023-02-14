package com.dor.cbn.service;

import java.sql.SQLException;

import com.dor.cbn.dto.AdminPurposeResponseDTO;
import com.dor.cbn.dto.DocumentRequestDTO;
import com.dor.cbn.dto.DocumentResponseDTO;
import com.dor.cbn.dto.DrugTypeRequestDTO;
import com.dor.cbn.dto.DrugTypeResponseDTO;
import com.dor.cbn.dto.PurposeRequestDTO;

public interface AdminMasterService {
	public AdminPurposeResponseDTO getAllPurposes() throws SQLException;
	public AdminPurposeResponseDTO addPurpose(PurposeRequestDTO requestDTO) throws SQLException;
	public AdminPurposeResponseDTO deletePurpose(PurposeRequestDTO requestDTO) throws SQLException;
	public AdminPurposeResponseDTO editPurpose(PurposeRequestDTO requestDTO) throws SQLException;
	public DocumentResponseDTO getAllDocuments() throws SQLException;
	public DocumentResponseDTO addDocument(DocumentRequestDTO requestDTO) throws SQLException;
	public DocumentResponseDTO deleteDocument(DocumentRequestDTO requestDTO) throws SQLException;
	public DocumentResponseDTO editDocument(DocumentRequestDTO requestDTO) throws SQLException;
	public DrugTypeResponseDTO getAllDrugTypes() throws SQLException;
	public DrugTypeResponseDTO addDrugType(DrugTypeRequestDTO requestDTO) throws SQLException;
	public DrugTypeResponseDTO deleteDrugType(DrugTypeRequestDTO requestDTO) throws SQLException;
	public DrugTypeResponseDTO editDrugType(DrugTypeRequestDTO requestDTO) throws SQLException;
}
