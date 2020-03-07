package ie.ucd.lms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoginConfig {

    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticationManagerBuilder auth;

    // @Bean
    // public PasswordEncoder getEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // public void login(HttpServletRequest request, String email, String password) {
    //     UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(email, password);
    //     Authentication auth = authenticationManager.authenticate(authReq);

    //     SecurityContext sc = SecurityContextHolder.getContext();
    //     sc.setAuthentication(auth);
    //     HttpSession session = request.getSession(true);
    //     session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", sc);
    // }

    // public AuthenticationManagerBuilder getAuth() {
    //     return auth;
    // }
}