package ie.ucd.lms.service;

import ie.ucd.lms.configuration.LoginConfig;
import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private LoginConfig loginConfig;

    // Debugging purposes
    // private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    public boolean exists(Login login) {
        String email = login.getEmail();
        String password = login.getHash();

        Login aLogin = loginRepository.findByEmail(email);

        if (aLogin == null) {
            return false;
        }

        return loginConfig.getEncoder().matches(password, aLogin.getHash());
    }

    public void save(Login login) {
        loginRepository.save(login);
    }

    public Login findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    public Login createLogin(String email, String password) {
        Login login = new Login();
        login.setEmail(email);
        login.setHash(loginConfig.getEncoder().encode(password));
        return login;
    }

    public ActionConclusion authenticate(String email, String password, boolean isLogin) {
        if (isLogin && emailExists(email)) {
            return new ActionConclusion(true, "Email does not exist");
        } else if (!isLogin && !emailExists(email)) {
            return new ActionConclusion(false, "Email already exists");
        } else {
            String msg = isLogin ? "Successfully Logged In " : "Successfully Signed Up";
            return new ActionConclusion(true, msg);
        }
    }

    private boolean emailExists(String email) {
        return loginRepository.findEmailByEmail(email) == null;
    }
}