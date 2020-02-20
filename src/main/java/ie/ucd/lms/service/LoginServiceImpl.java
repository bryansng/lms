package ie.ucd.lms.service;

import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepository loginRepository;
    
    @Override
    public void validate(Login login, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");

        if (loginRepository.getEmailByEmail(login.getEmail()) == null) {
            errors.rejectValue("email", "Duplicate.loginForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        String password = login.getHash();
        
        if (password.length() < 8) {
            errors.rejectValue("password", "Size.loginForm.password");
        }

        if (!password.equals(loginRepository.getPasswordByEmail(login.getEmail()))) {
            errors.rejectValue("wrongPassword", "Wrong.loginForm.wrongPassword");
        }
    }
}