package ie.ucd.lms.controller;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.LoginServiceImpl;
import ie.ucd.lms.service.MemberServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Controller
public class DefaultController {
  @Autowired
  private LoginServiceImpl loginServiceImpl;

  @Autowired
  private MemberServiceImpl memberServiceImpl;

  @Autowired
  // private BCryptPasswordEncoder bCryptPasswordEncoder;

  


  private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);
  @GetMapping("/")
  public String indexView() {
    return "index.html";
  }

  @GetMapping("/login")
  public String loginView() {
    return "member/login.html";
  }

  @PostMapping("/login") 
  public String loginMember(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {

    if (loginServiceImpl.exists(createLogin(email, password))) {
      return "index.html";
    }

    return "member/login.html";
  }

  @GetMapping("/register")
  public String registerView() {
    return "member/register";
  }

  @PostMapping("/register")
  public String registerMember(@RequestParam(name = "email") String email,
      @RequestParam(name = "password") String password) {    
    Login login = createLogin(email, password);
    Member member = createMember(email);

    logger.info(email);
    
    login.setMember(member);
    member.setLogin(login);

    memberServiceImpl.save(member);
    // loginServiceImpl.save(login);
   
    
    return "index.html";
  }
  
  public Login createLogin(String email, String password) {
    Login login = new Login();
    login.setEmail(email);
    // login.setHash(bCryptPasswordEncoder.encode(password));
    login.setHash(password);

    return login;
  }

  private Member createMember(String email) {
    Member member = new Member();
    member.setEmail(email);
    
    return member;
  }
}