package com.myhome.springCrudRestClient.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		UserDetails userDetails = userService.getByUsername(username).orElseThrow(IllegalArgumentException::new);
		System.out.println("===");
		System.out.println("login:" + userDetails.getUsername() + "; password:" + userDetails.getPassword());
		System.out.println("Authorities count: " + userDetails.getAuthorities().size());
		userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).forEach(System.out::println);
		System.out.println("===");

		return userDetails;
	}
}