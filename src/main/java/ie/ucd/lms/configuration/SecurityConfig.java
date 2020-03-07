package ie.ucd.lms.configuration;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.service.MemberDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	AuthenticationManager authenticationManager;

	@Autowired
	AuthenticationManagerBuilder auth;

	@Autowired
	MemberDetailsService memberDetailsService;

	public void configAuth(Login login, AuthenticationManagerBuilder auth, String role) {
		try {
			auth.inMemoryAuthentication().withUser(login.getEmail()).password(login.getHash()).roles(role);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AuthenticationManagerBuilder getAuth() {
		return auth;
	}

	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**").permitAll().antMatchers("/admin/**")
				.hasRole("ADMIN").antMatchers("/member/**").hasAnyRole("USER", "ADMIN").antMatchers("/").permitAll()
				.antMatchers("/h2-console/**").permitAll().and().formLogin().loginPage("/").loginProcessingUrl("/login")
				.successHandler(authenticationSuccessHandler).usernameParameter("email").passwordParameter("password").and()
				.logout().logoutSuccessUrl("/");

		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}