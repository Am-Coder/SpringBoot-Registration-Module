package com.example.herok.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.herok.service.MyUserDetailsService;

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
		.usersByUsernameQuery("select email,password,enabled from user where email=?")
		.authoritiesByUsernameQuery("select users_email,role from user_role inner join role on role.id = user_role.role_id  where users_email=?");
		
//		System.out.println("InMem\n");
//		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
//		.withUser("Aman").password("Aman").roles("USER")
//		.and()
//		.withUser("Aayu").password("Aayu").roles("ADMIN");
		
		
//		System.out.println("DAO\n");
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		provider.setUserDetailsService(userDetailsService);
//		provider.setPasswordEncoder(encode);
//		auth.authenticationProvider(provider);
		
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http 
			.headers()//XSS attacks protection
			
			.and()
			
			.csrf().disable()//Enable for CSRF protection
			.authorizeRequests()
			.antMatchers("/SignIn","/SignUp","/forgot","/registerUser","/verify","/reset").permitAll()//public url
			.antMatchers( "/RegistrationStyle/**").permitAll()			
			.antMatchers("/Home").hasAuthority("ROLE_USER")
			.anyRequest().authenticated()//protected url
			.and()
			
			//Custom Login
			.formLogin()
				.loginPage("/SignIn").permitAll()
				.defaultSuccessUrl("/success")
			.and()
			
			//Remeber Me Cookie (By default validity is 2 weeks)
			.rememberMe()
				.key("rem-me-key")
				.rememberMeParameter("remember-me")
				.rememberMeCookieName("MyCookie")
				.tokenValiditySeconds(30)
			
			.and()
			
			//Log Out configuration
			.logout()
				.permitAll()
				.invalidateHttpSession(true)
				.deleteCookies("MyCookie")//Delete Cookie created on logout
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/SignIn").permitAll();
			
		
		
		

	}
	
	
	//BCrypt Hash for password storing (default 10 rounds of hash)
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	

	
	
}
