package ie.ucd.lms.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring().requestMatchers(PathRequest.toH2Console());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable();
		// http
		// 	.authorizeRequests()
		// 		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
		// 		.anyRequest().authenticated()
		// 		.and()
		// 	.formLogin()
		// 		.permitAll();
		
		// Disabling default login page that comes with Spring security
		// http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "*/").permitAll()
		// 		.antMatchers(HttpMethod.GET, "/login").permitAll();

		http.csrf().disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/register").permitAll().antMatchers("/login").permitAll().anyRequest().authenticated();

		

    }
}