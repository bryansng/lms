package ie.ucd.lms.controller;

import ie.ucd.lms.configuration.SecurityConfig;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberDetailsService;
import ie.ucd.lms.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	MemberDetailsService memberDetailsService;

	@Autowired
	SecurityConfig securityConfig;

	@GetMapping("/restricted")
	public String restrictedView() {
		return "restricted";
	}

	@PostMapping("/register")
	public String registerMember(@RequestParam(name = "fullName") String fullName,
			@RequestParam(name = "email") String email, @RequestParam(name = "password") String password, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		ActionConclusion acMemberCreate = memberService.create(email, password, fullName, "", "", "", "", "", "member",
				"USER");
		if (acMemberCreate.isSuccess) {
			Member member = memberRepository.findByEmail(email);
			Login login = loginRepository.findByEmail(email);
			securityConfig.configAuth(login, securityConfig.getAuth(), "USER");
			authenticateUserAndSetSession(member, request);
		}

		redirectAttrs.addFlashAttribute("invalidCredentials", acMemberCreate.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", acMemberCreate.message);
		return "redirect:/";
	}

	private void authenticateUserAndSetSession(Member member, HttpServletRequest request) {
		UserDetails user = memberDetailsService.toUserDetails(member);

		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}