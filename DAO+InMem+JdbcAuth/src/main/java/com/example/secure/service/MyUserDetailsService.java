package com.example.secure.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.secure.enities.User;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(username);

		User user = repo.findByUsername(username);
		

		if(user==null)
			throw new UsernameNotFoundException("User 404");
		System.out.println(user.getRole());
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
			grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
		
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
//		return new UserPrincipal(user);
	}


	
	
}
