package ie.ucd.lms.service;

import ie.ucd.lms.entity.Login;
import org.springframework.validation.Errors;

public interface LoginService {
    void validate(Login login, Errors error);
}