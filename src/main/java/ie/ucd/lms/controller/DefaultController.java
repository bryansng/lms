package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ie.ucd.lms.configuration.SecurityConfig;
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

  @Autowired
  SecurityConfig securityConfig;

  @GetMapping("/")
  public String indexView(Model model, Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    model.addAttribute("latestArtifacts", artifactService.getLatestArtifacts());
    model.addAttribute("popularArtifacts", artifactService.getPopularArtifacts());
    return "index.html";
  }
}