package ie.ucd.lms.configuration;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.service.MemberDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// @Override
	// public void configure(WebSecurity web) {
	// 	web.ignoring().requestMatchers(PathRequest.toH2Console());
	// }

	// // @Override
	// // protected void configure(HttpSecurity http) throws Exception {
	// // 	// http.csrf().disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/register").permitAll()
	// // 	// .antMatchers("/login").permitAll().anyRequest().authenticated();

	// // }

	// public void configAuth(Login login, AuthenticationManagerBuilder auth, String role) {
	// 	try {
	// 		auth.inMemoryAuthentication().withUser(login.getEmail()).password(login.getHash()).roles(role);
	// 	} catch (Exception e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// }

	// @Override
	// public AuthenticationManager authenticationManager() throws Exception {
	// 	return super.authenticationManager();
	// }

	/**
	 *
	 *
	 *
	 *
	 *
	 *
	 */
	@Autowired
	MemberDetailsService memberDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/user").hasAnyRole("ADMIN", "USER")
				.antMatchers("/").permitAll().antMatchers("/h2-console/**").permitAll().and().formLogin();

		http.csrf().disable();
		http.headers().frameOptions().disable();
		// http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/user").hasAnyRole("ADMIN", "USER")
		//         .antMatchers("/").permitAll();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}