package com.dor.cbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dor.cbn.model.TokenEntity;

public interface TokenRepository extends JpaRepository<TokenEntity, String>{
	public TokenEntity findByLoginId(String loginId);
}
