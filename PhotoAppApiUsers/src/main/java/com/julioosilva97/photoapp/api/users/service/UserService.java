package com.julioosilva97.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.julioosilva97.photoapp.api.users.shared.UserDto;

public interface UserService extends UserDetailsService {
	
	public UserDto createUser(UserDto userDetails) ;
	public UserDto findByEmail(String email);
	public UserDto getUserByUserId(String userId);
}
