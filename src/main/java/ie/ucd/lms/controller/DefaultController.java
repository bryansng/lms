package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ArtifactService;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;

@Controller
public class DefaultController {
  @Autowired
  LoginService loginService;

  @Autowired
  MemberService memberService;

  @Autowired
  ArtifactService artifactService;

  @GetMapping("/")
  public String indexView(Model model, Authentication authentication) {
    if (authentication != null) {
      User user = (User) authentication.getPrincipal();
      Member member = memberService.findByEmail(user.getUsername());
      System.out.println("is authenticated");
      model.addAttribute("isAuthenticated", authentication.isAuthenticated());
      model.addAttribute("member", member);
    } else {
      System.out.println("not authenticated");
      model.addAttribute("isAuthenticated", false);
    }
    model.addAttribute("latestArtifacts", artifactService.getLatestArtifacts());
    model.addAttribute("popularArtifacts", artifactService.getPopularArtifacts());
    return "index.html";
  }

  @GetMapping("/user")
  @ResponseBody
  public String user() {
    return ("<h1>Welcome User</h1>");
  }

  @GetMapping("/admin")
  @ResponseBody
  public String admin() {
    return ("<h1>Welcome Admin</h1>");
  }
}