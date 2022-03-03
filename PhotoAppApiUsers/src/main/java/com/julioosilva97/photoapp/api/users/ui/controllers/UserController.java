package com.julioosilva97.photoapp.api.users.ui.controllers;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.julioosilva97.photoapp.api.users.service.UserService;
import com.julioosilva97.photoapp.api.users.shared.UserDto;
import com.julioosilva97.photoapp.api.users.ui.model.AlbumResponseModel;
import com.julioosilva97.photoapp.api.users.ui.model.UserRequest;
import com.julioosilva97.photoapp.api.users.ui.model.UserResponse;
import com.julioosilva97.photoapp.api.users.ui.model.UserResponseModel;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment env;
	
	@Value("${teste.teste}")
	private String test;

	@Autowired
	private ModelMapper map;

	@Autowired
	private UserService service;
	


	@GetMapping("/working")
	public String getWorking() {
		return "Port: " + env.getProperty("local.server.port") + " e properties:" + test;
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequestBody) {

		UserDto userDto = map.map(userRequestBody, UserDto.class);

		UserDto savedUser = service.createUser(userDto);
		UserResponse response = map.map(savedUser, UserResponse.class);
		

		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{userId}")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId){
		
		UserDto userDto = service.getUserByUserId(userId);
		UserResponseModel response = map.map(userDto, UserResponseModel.class);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}

}
