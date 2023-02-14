package com.dor.cbn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dor.cbn.service.JwtUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/api/v1/authenticate","/generate/session","/destroy/session","/api/v1/users/fetch",
						"/api/v1/generate/otp","/api/v1/validate/otp","/api/v1/send/mail","/api/v1/verify/mail","/api/v1/set/password",
						"/api/v1/user/login","/api/v1/login/generate/otp","/api/v1/register","/api/v1/verify/user",
						"/api/v1/updateProfileData","/api/v1/admin/master/fetch/purpose/data","/api/v1/admin/master/add/purpose/data",
						"/api/v1/admin/master/delete/purpose/data","/api/v1/admin/master/edit/purpose/data","/api/v1/admin/master/fetch/document/list","/api/v1/admin/master/add/document",
						"/api/v1/admin/master/edit/document","/api/v1/admin/master/delete/document", "/api/v1/admin/master/fetch/drug/type", "/api/v1/admin/master/add/drug/type",
						"/api/v1/admin/master/delete/drug/type","/api/v1/admin/master/edit/drug/type","/api/v1/admin/login/generate/otp", "/api/v1/admin/user/login",
						"/api/v1/admin/verify/user","/api/v1/admin/set/password","/api/v1/admin/generate/otp","/api/v1/admin/validate/otp","/api/v1/admin/verify/mail",
						"/api/v1/profile/fetch/substance/data","/api/v1/profile/save/plant/data","/api/v1/profile/update/plant/data").permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

