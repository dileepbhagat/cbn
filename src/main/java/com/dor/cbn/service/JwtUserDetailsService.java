package com.dor.cbn.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dor.cbn.model.AdminEntity;
import com.dor.cbn.model.LoginEntity;
import com.dor.cbn.repository.AdminRepository;
import com.dor.cbn.repository.LoginRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		String password="";
		int index=loginId.indexOf("@");
		if(index!=-1)
		{
			// AdminEntity class
			AdminEntity adminEntity= adminRepository.findByLoginId(loginId);
			if(adminEntity!=null)
				password=adminEntity.getPassword();
			else
				throw new UsernameNotFoundException("User not found with loginId: " + loginId);
		}
		else
		{
			LoginEntity loginEntity = loginRepository.findByLoginId(loginId);
			if (loginEntity == null) {
				throw new UsernameNotFoundException("User not found with loginId: " + loginId);
			}
			else
				password=loginEntity.getPassword();
		}
		return new org.springframework.security.core.userdetails.User(loginId, new BCryptPasswordEncoder().encode(password),
				new ArrayList<>());
	}
	
}

