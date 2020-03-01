package ie.ucd.lms.controller;

import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ArtifactService;
import ie.ucd.lms.service.LoanHistoryService;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DefaultController {
  @Autowired
  private LoginService loginService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private ArtifactService artifactService;

  @Autowired
  private LoanHistoryService LoanHistoryService;

  // Debugging purposes
  private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

  @GetMapping("/restricted")
  public String restrictedView() {
    return "restricted";
  }

  @PostMapping("/member/profile")
  public String profileView(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult, Model model,
      RedirectAttributes redirectAttrs) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("invalid", "invalid");
    }

    return "/member/profile";
  }

  @GetMapping("member/view")
  public String artifactView(@RequestParam(name = "id") Long id, Model model) {
    Optional<Artifact> viewArtifact = artifactService.exists(id);

    if (viewArtifact.isPresent()) {
      model.addAttribute("artifact", viewArtifact.get());
    }

    return "member/view";
  }

  @GetMapping("member/historical")
  public String historicalView(Model model) {
    return "member/historical";
  }

  @GetMapping("member/loans")
  public String loansView(Model model) {
    // final version will take member entity as parameters from redirectattrs
    Member member = memberService.findByEmail("hong.sng@ucdconnect.ie");
    List<LoanHistory> loans = LoanHistoryService.findByMember(member);
    model.addAttribute("member", member);
    model.addAttribute("loans", loans);
    return "member/loans";
  }

  @GetMapping("member/index")
  public String testView(Model model) {
    model.addAttribute("latestArtifacts", artifactService.getLatestArtifacts());
    model.addAttribute("popularArtifacts", artifactService.getPopularArtifacts());
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
      HttpServletRequest request, RedirectAttributes redirectAttrs) {

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