package com.julioosilva97.photoapp.api.users.feignclient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.julioosilva97.photoapp.api.users.ui.model.AlbumResponseModel;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "albums-ws") 
public interface AlbumsServiceClient {
	
	@GetMapping("/users/{id}/albums")
	@Retry(name = "albums-ws")
	@CircuitBreaker(name = "albums-ws", fallbackMethod = "getAlbumsFallback")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
	
	default List<AlbumResponseModel> getAlbumsFallback(String id, Throwable exThrowable){
		System.out.println("Param ="+ id);
		System.out.println("Excption  ="+ exThrowable.getMessage());
		return new ArrayList<>();
		
	}
}
