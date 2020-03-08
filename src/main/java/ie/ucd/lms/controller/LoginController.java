package ie.ucd.lms.controller;

import ie.ucd.lms.configuration.SecurityConfig;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	@Autowired
	LoginService loginService;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	SecurityConfig securityConfig;

	@GetMapping("/cheat/login")
	public String autoLogin(@RequestParam(defaultValue = "hong.sng@ucdconnect.ie") String email,
			HttpServletRequest request) {
		Member member = memberRepository.findByEmail(email);
		Login login = loginRepository.findByEmail(email);
		securityConfig.configAuth(login, securityConfig.getAuth(), "ADMIN");
		loginService.authenticateUserAndSetSession(member, request);
		return "redirect:/";
	}

	@GetMapping("/cheat/regis")
	public String autoRegister(@RequestParam(defaultValue = "d d d d d d d") String fullName,
			@RequestParam(defaultValue = "d@d.d") String email, @RequestParam(defaultValue = "1234") String password,
			@RequestParam(defaultValue = "1234") String confirmPassword, HttpServletRequest request) {
		ActionConclusion acMemberCreate = memberService.create(email, password, fullName, "", "", "", "", "", "member",
				"USER");
		if (acMemberCreate.isSuccess) {
			Member member = memberRepository.findByEmail(email);
			Login login = loginRepository.findByEmail(email);
			securityConfig.configAuth(login, securityConfig.getAuth(), "USER");
			loginService.authenticateUserAndSetSession(member, request);
		}
		return "redirect:/";
	}

	@PostMapping("/login")
	public String loginMember(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		ActionConclusion actionConclusion = loginService.login(email, password, request);

		redirectAttrs.addFlashAttribute("invalidCredentials", actionConclusion.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", actionConclusion.message);
		return "redirect:/";
	}

	@PostMapping("/register")
	public String registerMember(@RequestParam(name = "fullName") String fullName,
			@RequestParam(name = "email") String email, @RequestParam(name = "password") String password,
			@RequestParam(name = "confirmPassword") String confirmPassword, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		ActionConclusion actionConclusion = loginService.register(fullName, email, password, confirmPassword, request);

		redirectAttrs.addFlashAttribute("invalidCredentials", actionConclusion.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", actionConclusion.message);
		return "redirect:/";
	}
}