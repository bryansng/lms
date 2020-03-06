package ie.ucd.lms.controller;

import ie.ucd.lms.configuration.LoginConfig;
import ie.ucd.lms.configuration.SecurityConfig;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
	MemberService memberService;

	@Autowired
	LoginConfig loginConfig;

	@Autowired
	SecurityConfig securityConfig;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/restricted")
	public String restrictedView() {
		return "restricted";
	}

	// @GetMapping("/login")
	// public String loginView(Login login) {
	// 	return "member/login";
	// }

	// @PostMapping("/login")
	// public String loginMember(@Valid @ModelAttribute("member") Login login, BindingResult bindingResult, Model model,
	// 		HttpServletRequest request, RedirectAttributes redirectAttrs) {

	// 	if (bindingResult.hasErrors() || !loginService.exists(login)) {
	// 		model.addAttribute("loginError", true);
	// 		return "member/login";
	// 	}

	// 	Member member = memberService.findByEmail(login.getEmail());

	// 	redirectAttrs.addFlashAttribute("member", member);

	// 	HttpSession session = request.getSession();

	// 	model.addAttribute("isAuthenticated", true);

	// 	return "redirect:/member/profile";
	// }

	@PostMapping("/login")
	public String loginMember(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {

		ActionConclusion ac = loginService.authenticate(email, password, true);
		if (ac.isSuccess) {
			Member member = memberService.findByEmail(email);
			Login login = loginService.findByEmail(email);
			redirectAttrs.addFlashAttribute("member", member);
			redirectAttrs.addFlashAttribute("isAuthenticated", true);
			authenticateUserAndSetSession(login, request);
		}

		redirectAttrs.addFlashAttribute("invalidCredentials", ac.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", ac.message);

		return "redirect:/";
	}

	// @GetMapping("/register")
	// public String registerView(Login loginModel) {
	// 	return "member/register";
	// }

	@PostMapping("/register")
	public String registerMember(@RequestParam(name = "fullName") String fullName,
			@RequestParam(name = "email") String email, @RequestParam(name = "password") String password, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {

		ActionConclusion ac = loginService.authenticate(email, password, false);
		if (ac.isSuccess) {
			logger.info(ac.message);
			Login login = loginService.createLogin(email, password);
			Member member = memberService.createMember(login);
			memberService.save(member, login);
			securityConfig.configAuth(login, loginConfig.getAuth(), "USER");
			authenticateUserAndSetSession(login, request);
		}

		redirectAttrs.addFlashAttribute("invalidCredentials", ac.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", ac.message);
		return "redirect:/";
	}

	private void authenticateUserAndSetSession(Login login, HttpServletRequest request) {
		String username = login.getEmail();
		String password = login.getHash();
		logger.info(password);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		// generate session if one doesn't exist
		request.getSession();

		token.setDetails(new WebAuthenticationDetails(request));

		try {
			Authentication authenticatedUser = securityConfig.authenticationManager().authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @PostMapping("/register")
	// public String registerMember(@Valid Login login, BindingResult bindingResult, Model model) {
	// 	if (bindingResult.hasErrors()) {
	// 		model.addAttribute("invalidLogin", true);
	// 	} else if (loginService.exists(login)) {
	// 		model.addAttribute("emailExists", true);
	// 	} else {
	// 		Login newLogin = loginService.createLogin(login);
	// 		Member member = memberService.createMember(login);

	// 		memberService.save(member, newLogin);

	// 		return "redirect:/login";
	// 	}

	// 	return "member/register";
	// }
}