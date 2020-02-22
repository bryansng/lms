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

@Controller
public class DefaultController {
  @Autowired
  private LoginServiceImpl loginServiceImpl;

  @Autowired
  private MemberServiceImpl memberServiceImpl;

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
  public String loginMember(@RequestParam(name = "email") String email,
      @RequestParam(name = "password") String password) {

    return "member/login.html";
  }

  @GetMapping("/register")
  public String registerView() {
    return "member/register";
  }

  @PostMapping("/register")
  public String registerMember(@RequestParam(name = "email") String email,
      @RequestParam(name = "password") String password) {
    Login login = loginServiceImpl.createLogin(email, password);
    Member member = memberServiceImpl.createMember(email);

    memberServiceImpl.save(member, login);

    return "redirect:/";
  }
}