package com.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bank.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
//	@Qualifier("userService")
	private UserService userService;

	private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(PASSWORD_ENCODER);
	}

	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().sessionManagement().disable()// n1
				.authorizeRequests().antMatchers("/user-managment/hello") // n1
				.permitAll()
//				.hasAuthority("ADMIN")// n1
				.antMatchers("/account-managment/findAll").permitAll()// n1
				.anyRequest().authenticated()// n1
				.and().formLogin().defaultSuccessUrl("/swagger-ui.html").permitAll()// n1
				.and().logout()// n1
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))// n1
				.logoutSuccessUrl("/login").permitAll();// n1

	}

}
