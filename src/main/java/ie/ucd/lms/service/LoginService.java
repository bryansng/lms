package ie.ucd.lms.service;

import ie.ucd.lms.entity.Login;

public interface LoginService {

    boolean exists(Login login);

    void save(Login login);

    Login createLogin(Login login);
}