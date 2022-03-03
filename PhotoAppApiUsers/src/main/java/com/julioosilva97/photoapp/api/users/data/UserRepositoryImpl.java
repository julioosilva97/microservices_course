package com.julioosilva97.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryImpl extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByEmail(String email);

	public UserEntity findByUserId(String userId);

}
