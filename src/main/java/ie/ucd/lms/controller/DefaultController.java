package ie.ucd.lms.controller;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DefaultController {
  @Autowired
  private LoginService loginService;

  @Autowired
  private MemberService memberService;

  // Debugging purposes
  private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

  @GetMapping("/restricted")
  public String restrictedView() {
    return "restricted";
  }

  @GetMapping("member/historical")
  public String historicalView(Model model) {
    return "member/historical";
  }

  @GetMapping("member/loans")
  public String loansView(Model model) {

    return "member/loans";
  }

  @GetMapping("member/index")
  public String testView() {
    return "member/index";
  }

  @GetMapping("/member/profile")
  public String indexView(@ModelAttribute("member") Member member, Model model) {
    model.addAttribute("member", member);

    return "member/index.html";
  }

  @GetMapping("/login")
  public String loginView(Login login) {
    return "member/login";
  }

  @PostMapping("/login")
  public String loginMember(@Valid @ModelAttribute("member") Login login, BindingResult bindingResult, Model model,
      ModelMap modelMap, HttpServletRequest request, RedirectAttributes redirectAttrs) {

    if (bindingResult.hasErrors() || !loginService.exists(login)) {
      model.addAttribute("loginError", true);
      return "member/login";
    }

    Member member = memberService.findByEmail(login.getEmail());

    redirectAttrs.addFlashAttribute("member", member);

    HttpSession session = request.getSession();

    return "redirect:/member/profile";
  }

  @GetMapping("/register")
  public String registerView(Login loginModel) {
    return "member/register";
  }

  @PostMapping("/register")
  public String registerMember(@Valid Login login, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("invalidLogin", true);
    } else if (loginService.exists(login)) {
      model.addAttribute("emailExists", true);
    } else {
      Login newLogin = loginService.createLogin(login);
      Member member = memberService.createMember(login);

      memberService.save(member, newLogin);

      return "redirect:/login";
    }

    return "member/register";
  }

  @GetMapping("/admin/index.html")
  public String testIndexView() {
    tests();

    return "admin/index.html";
  }

  public void tests() {
    // testMemberExists();
  }

  // public void testMemberExists() {
  //   Assert.isTrue(memberService.search("email", 0).getSize() == 1, "register method incorrect");
  // }

}