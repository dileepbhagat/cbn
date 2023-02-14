package com.dor.cbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dor.cbn.model.UserRoleTokenEntity;

public interface UserRoleTokenRepository extends JpaRepository<UserRoleTokenEntity, String>{
	public UserRoleTokenEntity findByUserRole(String userRole);
}

