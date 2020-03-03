package ie.ucd.lms.controller;

import ie.ucd.lms.configuration.LoginConfig;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.LoginService;
import ie.ucd.lms.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	MemberService memberService;

	@Autowired
	LoginConfig loginConfig;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/restricted")
	public String restrictedView() {
		return "restricted";
	}

	@GetMapping("/login")
	public String loginView(Login login) {
		return "member/login";
	}

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
			redirectAttrs.addFlashAttribute("member", member);
			redirectAttrs.addFlashAttribute("isAuthenticated", true);
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
		}

		redirectAttrs.addFlashAttribute("invalidCredentials", ac.isSuccess);
		redirectAttrs.addFlashAttribute("onClick", true);
		redirectAttrs.addFlashAttribute("credentialsMsg", ac.message);
		return "redirect:/";
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