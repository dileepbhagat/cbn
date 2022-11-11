package com.dor.cbn.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer>{
	public UsersEntity findByLoginid(String loginid);
}
