package com.julioosilva97.photoapp.api.users.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.julioosilva97.photoapp.api.users.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private Environment environment;

	private UserService userService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		
		this.environment = environment;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
		http.authorizeRequests().antMatchers("/**").permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/circuitbrekerevents").permitAll()
		.and()
		.addFilter(getAuthenticationFilter());
		http.csrf().disable();
		http.headers().frameOptions().disable();

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	
	
	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment,userService,authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));;
		//authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}
}
