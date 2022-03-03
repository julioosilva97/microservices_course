package com.julioosilva97.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.julioosilva97.photoapp.api.users.data.UserEntity;
import com.julioosilva97.photoapp.api.users.data.UserRepositoryImpl;
import com.julioosilva97.photoapp.api.users.feignclient.AlbumsServiceClient;
import com.julioosilva97.photoapp.api.users.shared.UserDto;
import com.julioosilva97.photoapp.api.users.ui.model.AlbumResponseModel;

@Service
public class UserServiceImpl implements UserService {

	ModelMapper map;

	UserRepositoryImpl repository;

	BCryptPasswordEncoder bCryptPasswordEncoder;

	//@Autowired
	//RestTemplate restTemplate;

	Environment environment;
	
	AlbumsServiceClient albumsServiceClient;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserServiceImpl(ModelMapper map, UserRepositoryImpl repository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment,AlbumsServiceClient albumsServiceClient) {
		this.map = map;
		this.repository = repository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.environment = environment;
		this.albumsServiceClient = albumsServiceClient;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {

		userDetails.setUserId(UUID.randomUUID().toString());

		UserEntity userEntity = map.map(userDetails, UserEntity.class);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		repository.save(userEntity);

		UserDto savedUser = map.map(userEntity, UserDto.class);
		return savedUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity user = repository.findByEmail(username);

		if (user == null)
			throw new UsernameNotFoundException(username);

		return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto findByEmail(String email) {

		UserEntity user = repository.findByEmail(email);

		if (user == null)
			throw new UsernameNotFoundException(email);

		return map.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity user = repository.findByUserId(userId);

		if (user == null)
			throw new UsernameNotFoundException(userId);

		UserDto userDto = map.map(user, UserDto.class);

		try {

			/*String albumUrl = String.format(environment.getProperty("albuns.url"), userId);
			
			  ResponseEntity<List<AlbumResponseModel>> albunsListResponse = restTemplate
			  .exchange(albumUrl, HttpMethod.GET, null, new
			  ParameterizedTypeReference<List<AlbumResponseModel>>(){});
			 

			List<AlbumResponseModel> albuns = albunsListResponse.getBody();*/
			
			logger.info("Antes de chamar microserviço de albums");
			List<AlbumResponseModel> albuns = albumsServiceClient.getAlbums(userId);
			logger.info("Depois de chamar microserviço de albums");

			userDto.setAlbums(albuns);
			return userDto;

		} catch (Exception e) {
			System.out.print(e.getMessage());
			return null;
		}

	}

}
