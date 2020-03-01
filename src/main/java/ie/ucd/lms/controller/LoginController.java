package ie.ucd.lms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.service.MemberService;
import ie.ucd.lms.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	LoginService loginService;

	@Autowired
	MemberService memberService;

	@GetMapping("/restricted")
	public String restrictedView() {
		return "restricted";
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
}