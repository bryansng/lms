package ie.ucd.lms.controller;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.LoginServiceImpl;
import ie.ucd.lms.service.MemberServiceImpl;

import javax.validation.Valid;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DefaultController {
  @Autowired
  private LoginServiceImpl loginServiceImpl;

  @Autowired
  private MemberServiceImpl memberServiceImpl;

  // Debugging purposes
  // private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

  @GetMapping("/")
  public String indexView() {
    return "index.html";
  }

  @GetMapping("/login")
  public String loginView(Login loginModel) {
    return "member/login";
  }

  @PostMapping("/login")
  public String loginMember(@Valid Login login, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors() || !loginServiceImpl.exists(login)) {
      model.addAttribute("loginError", true);
      return "member/login";
    }

    return "redirect:/";
  }

  @GetMapping("/register")
  public String registerView(Login loginModel) {
    return "member/register";
  }

  @PostMapping("/register")
  public String registerMember(@Valid Login login, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("invalidLogin", true);
    } else if (loginServiceImpl.exists(login)) {
      model.addAttribute("emailExists", true);
    } else {
      Login newLogin = loginServiceImpl.createLogin(login);
      Member member = memberServiceImpl.createMember(login);

      memberServiceImpl.save(member, newLogin);

      return "redirect:/";
    }

    return "member/register";
  }
}