package ie.ucd.lms.controller;

import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.ReserveQueue;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.ArtifactService;
import ie.ucd.lms.service.Common;
import ie.ucd.lms.service.LoanHistoryService;
import ie.ucd.lms.service.MemberService;
import ie.ucd.lms.service.ReserveQueueService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {
	@Autowired
	MemberService memberService;

	@Autowired
	ArtifactService artifactService;

	@Autowired
	LoanHistoryService LoanHistoryService;

	@Autowired
	ReserveQueueService reserveQueueService;

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

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
		List<ReserveQueue> reservedLoans = reserveQueueService.getReservedLoansForMember(member);
		// logger.info("loans: " + loans.toString());
		List<LoanHistory> historicalLoans = LoanHistoryService.getHistoricalLoans(member);
		// Page<LoanHistory> historicalLoans = LoanHistoryService.getHistorialLoans(member);

		// logger.info(historicalLoans.toString());
		model.addAttribute("member", member);

		model.addAttribute("loans", loans);
		model.addAttribute("historicalLoans", historicalLoans);
		model.addAttribute("reservedLoans", reservedLoans);
		return "member/loans";
	}

	@GetMapping("/member/profile")
	public String indexView(@ModelAttribute("member") Member member, Model model) {
		model.addAttribute("member", member);

		return "index.html";
	}

	@GetMapping("/member/reserve")
	public String artifactReserve(@RequestParam(name = "id", value = "id", required = true) Long id,
			@RequestParam(name = "isbn") String isbn, Model model, RedirectAttributes redirectAttrs) {
		// will pass member as a parameter once authentication is set up
		logger.info(Long.toString(id));
		logger.info(isbn);
		Member member = memberService.findByEmail("hong.sng@ucdconnect.ie");
		// finding out what expiredOn is
		// ActionConclusion ac = reserveQueueService.create(isbn, Long.toString(member.getId()), "21/03/20");
		redirectAttrs.addFlashAttribute("reserve", true);
		// redirectAttrs.addFlashAttribute("reserveMsg", ac.message);
		// redirectAttrs.addFlashAttribute("reserveFailed", ac.isSuccess);
		return "redirect:/";
	}

	@GetMapping("/admin/members/view")
	public String membersView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<Member> members = memberService.search(searchQuery, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - members.getTotalElements());
		model.addAttribute("totalPages", members.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("members", members);

		model.addAttribute("previousQuery", searchQuery);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "admin/member/view.html";
	}

	@GetMapping("/admin/members/edit")
	public String membersEdit(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery, Model model) {
		return "admin/member/view.html";
	}

	@GetMapping("/member/renew")
	public String artifactRenew(@RequestParam(name = "id", value = "id", required = true) Long id,
			@RequestParam(name = "days", value = "days") String days, Model model, RedirectAttributes redirectAttrs) {
		// logger.info("Long id " + Long.toString(id));
		logger.info(Long.toString(id));
		logger.info("days: " + days);
		ActionConclusion ac = LoanHistoryService.renew(Long.toString(id), days);

		redirectAttrs.addFlashAttribute("renewalFailed", ac.isSuccess);
		redirectAttrs.addFlashAttribute("renewal", true);
		redirectAttrs.addFlashAttribute("renewalMsg", ac.message);

		return "redirect:/member/loans";
	}

	// @PostMapping("/admin/members/delete")
	// @ResponseBody
	// public String membersDelete(@RequestParam(name = "id") String stringId, Model model) {
	// 	return memberService.delete(stringId).toString();
	// }
}