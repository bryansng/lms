package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
  public String indexView(Model model) {
    model.addAttribute("latestArtifacts", artifactService.getLatestArtifacts());
    model.addAttribute("popularArtifacts", artifactService.getPopularArtifacts());
    return "index.html";
  }
}