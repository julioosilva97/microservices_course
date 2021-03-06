package com.julioosilva97.photoapp.api.users.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julioosilva97.photoapp.api.users.service.UserService;
import com.julioosilva97.photoapp.api.users.shared.UserDto;
import com.julioosilva97.photoapp.api.users.ui.model.LoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private Environment environment;
	private UserService userService;
	
	@Autowired
	 public AuthenticationFilter(Environment environment,UserService userService,AuthenticationManager authenticationManager) {
		 this.environment = environment; 
		 this.userService = userService;
		 super.setAuthenticationManager(authenticationManager);
	 }
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequestModel creds = new ObjectMapper()
					.readValue(request.getInputStream(), LoginRequestModel.class);
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(), 
							creds.getPassword(),
							new ArrayList<>()
							)
					);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		
		String userName = ((User)authResult.getPrincipal()).getUsername();
		UserDto userDetails = userService.findByEmail(userName);
		
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();
		
		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
		
		
		//super.successfulAuthentication(request, response, chain, authResult);
	}
}
