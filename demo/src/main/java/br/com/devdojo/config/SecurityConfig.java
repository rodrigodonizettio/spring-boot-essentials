package br.com.devdojo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.devdojo.service.CustomUserDetailService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/*/admin/**").hasRole("ADMIN")
			.antMatchers("/*/protected/**").hasRole("USER")
			.and()
			.httpBasic()
			.and()
			.csrf().disable(); //Cross-Site Request Forgery (Requests feitas por uma aplicação maliciosa que se aproveita de alguma sessão de usuário autenticado ativa no sistema).
							   //REMOVER CSRF-DISABLE para ambiente de produção.
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
//UTILIZADO PARA BANCO EM MEMÓRIA (CACHE)
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		auth.inMemoryAuthentication()
//			.withUser("rodrigo").password(encoder.encode("devdojo")).roles("USER")
//			.and()
//			.withUser("admin").password(encoder.encode("devdojo")).roles("USER", "ADMIN");
//	}
}
