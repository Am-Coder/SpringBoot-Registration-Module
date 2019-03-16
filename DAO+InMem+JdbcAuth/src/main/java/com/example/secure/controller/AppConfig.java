package com.example.secure.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.secure.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired 
	private DataSource dataSource; 
	
	@Autowired
	private BCryptPasswordEncoder encode;
//	@Bean
//	public AuthenticationProvider authProvider() {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		
//		//provider talk to sevice and then servie to DAO
//		provider.setUserDetailsService(userDetailsService);
//		provider.setPasswordEncoder(encoder);
//		
//		
//		return provider;
//	}
	
	@Autowired 
	public void configuringAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
		System.out.println("Jdbc\n");
		auth.jdbcAuthentication().passwordEncoder(encode).dataSource(dataSource)
		.usersByUsernameQuery("select username,password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select username, role from user_roles where username=?");
		
		System.out.println("InMem\n");
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
		.withUser("Aman").password("Aman").roles("USER")
		.and()
		.withUser("Aayu").password("Aayu").roles("ADMIN");
		
		
		System.out.println("DAO\n");
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(encode);
		auth.authenticationProvider(provider);
		
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/").permitAll()//public url
			.antMatchers("/success").hasAuthority("ROLE_ADMIN")
			.anyRequest().authenticated()//protected url
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.defaultSuccessUrl("/success")
			.and()
			.logout().invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/logmeout").permitAll();
			
		
		
		

	}
	
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	
//	@Bean
//	@Override
//	protected UserDetailsService userDetailsService() {
//		
//		List<UserDetails> users = new ArrayList();
//		users.add(User.withDefaultPasswordEncoder().username("Aman").password("Aman").roles("USER").build());
//		users.add(User.withDefaultPasswordEncoder().username("Ama").password("Ama").roles("USER").build());
//		
//		return new InMemoryUserDetailsManager(users);
//	}
//	
	
	
}
