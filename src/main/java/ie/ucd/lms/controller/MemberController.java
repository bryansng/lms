package ie.ucd.lms.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.service.Common;
import ie.ucd.lms.service.MemberService;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.ArtifactService;
import ie.ucd.lms.service.LoanHistoryService;

@Controller
public class MemberController {
	@Autowired
	MemberService memberService;

	@Autowired
	ArtifactService artifactService;

	@Autowired
	LoanHistoryService LoanHistoryService;

	@PostMapping("/member/profile")
	public String profileView(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("invalid", "invalid");
		}

		return "/member/profile";
	}

	@GetMapping("/member/view")
	public String artifactView(@RequestParam(name = "id") Long id, Model model) {
		Optional<Artifact> viewArtifact = artifactService.exists(id);

		if (viewArtifact.isPresent()) {
			model.addAttribute("artifact", viewArtifact.get());
		}

		return "member/view";
	}

	@GetMapping("/member/historical")
	public String historicalView(Model model) {
		return "member/historical";
	}

	@GetMapping("/member/loans")
	public String loansView(Model model) {
		// final version will take member entity as parameters from redirectattrs
		Member member = memberService.findByEmail("hong.sng@ucdconnect.ie");
		List<LoanHistory> loans = LoanHistoryService.findByMember(member);
		model.addAttribute("member", member);
		model.addAttribute("loans", loans);
		return "member/loans";
	}

	@GetMapping("/member/profile")
	public String indexView(@ModelAttribute("member") Member member, Model model) {
		model.addAttribute("member", member);

		return "index.html";
	}

	@GetMapping("/admin/members/view")
	public String membersView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String isSuccess,
			@RequestParam(defaultValue = "", required = false) String successMessage,
			@RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
		Page<Member> members = memberService.search(searchQuery, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - members.getTotalElements());
		model.addAttribute("totalPages", members.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("members", members);

		model.addAttribute("previousQuery", searchQuery);
		model.addAttribute("previousIsSuccess", isSuccess);
		model.addAttribute("previousSuccessMessage", successMessage);
		model.addAttribute("previousFailureMessage", failureMessage);
		return "admin/member/view.html";
	}

	@GetMapping("/admin/members/edit")
	public String membersEdit(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery, Model model) {
		return "admin/member/view.html";
	}

	@PostMapping("/admin/members/delete")
	@ResponseBody
	public ActionConclusion membersDelete(@RequestParam(name = "id") String stringId, Model model) {
		return memberService.delete(stringId);
	}
}