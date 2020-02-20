package ie.ucd.lms.service;

import ie.ucd.lms.entity.Login;
import org.springframework.validation.Errors;

public interface LoginServiceInterface {
    void validate(Login login, Errors error);
}