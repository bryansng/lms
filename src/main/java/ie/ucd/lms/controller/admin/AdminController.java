package ie.ucd.lms.controller.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.MemberService;
import ie.ucd.lms.service.admin.LoanHistoryService;
import ie.ucd.lms.service.admin.ReserveQueueService;
import ie.ucd.lms.service.admin.ReportService;

@Controller
public class AdminController {
	@Autowired
	ArtifactRepository artifactRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	ArtifactService artifactService;

	@Autowired
	LoanHistoryService loanHistoryService;

	@Autowired
	ReserveQueueService reserveQueueService;

	@Autowired
	ReportService reportService;

	@GetMapping("/admin/dashboard")
	public String dashboardView(Model model) {
		model.addAttribute("todayFine", reportService.fine(reportService.today));
		model.addAttribute("monthFine", reportService.fine(reportService.thisMonth));
		model.addAttribute("yearFine", reportService.fine(reportService.thisYear));
		model.addAttribute("todayArtifacts", reportService.totalArtifacts(reportService.today));
		model.addAttribute("monthArtifacts", reportService.totalArtifacts(reportService.thisMonth));
		model.addAttribute("yearArtifacts", reportService.totalArtifacts(reportService.thisYear));
		model.addAttribute("todayIssued", reportService.artifactsIssued(reportService.today));
		model.addAttribute("monthIssued", reportService.artifactsIssued(reportService.thisMonth));
		model.addAttribute("yearIssued", reportService.artifactsIssued(reportService.thisYear));
		model.addAttribute("todayReturned", reportService.artifactsReturned(reportService.today));
		model.addAttribute("monthReturned", reportService.artifactsReturned(reportService.thisMonth));
		model.addAttribute("yearReturned", reportService.artifactsReturned(reportService.thisYear));
		model.addAttribute("todayLost", reportService.artifactsLost(reportService.today));
		model.addAttribute("monthLost", reportService.artifactsLost(reportService.thisMonth));
		model.addAttribute("yearLost", reportService.artifactsLost(reportService.thisYear));
		return "admin/dashboard/dashboard.html";
	}

	@GetMapping("/admin/members/view")
	public String membersView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<Member> members = memberService.search(searchQuery, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - members.getTotalElements());
		model.addAttribute("totalPages", members.getTotalPages());
		model.addAttribute("currentPage", page + 1);
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

	// @PostMapping("/admin/members/delete")
	// @ResponseBody
	// public String membersDelete(@RequestParam(name = "id") String stringId, Model model) {
	// 	return memberService.delete(stringId).toString();
	// }

	@GetMapping("/admin/artifacts/view")
	public String artifactsView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String type,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<Artifact> artifacts = artifactService.search(searchQuery, type, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
		model.addAttribute("totalPages", artifacts.getTotalPages());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("artifacts", artifacts);

		model.addAttribute("previousQuery", searchQuery);
		model.addAttribute("previousType", type);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "admin/artifact/view.html";
	}

	@GetMapping("/admin/artifacts/edit")
	public String artifactsEditGet(@RequestParam(name = "id") String stringId, Model model) {
		model.addAttribute("artifact", artifactRepository.findById(Common.convertStringToLong(stringId)));
		return "admin/artifact/edit.html";
	}

	// @PostMapping("/admin/artifacts/edit")
	// public String artifactsEditPost(@RequestParam(name = "id") String stringId, Model model) {
	// 	return artifactService.update(stringId).toString();
	// }

	@PostMapping("/admin/artifacts/delete")
	@ResponseBody
	public String artifactsDelete(@RequestParam(name = "id") String stringId, Model model) {
		return artifactService.delete(stringId).toString();
	}

	@GetMapping("/admin/loans/view")
	public String loansView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String artifactQuery,
			@RequestParam(defaultValue = "", required = false) String memberQuery,
			@RequestParam(defaultValue = "", required = false) String fromDate,
			@RequestParam(defaultValue = "", required = false) String toDate,
			@RequestParam(defaultValue = "", required = false) String dateType,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<LoanHistory> loans = loanHistoryService.searchAllButLost(artifactQuery, memberQuery, fromDate, toDate,
				dateType, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
		model.addAttribute("totalPages", loans.getTotalPages());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("loans", loans);
		model.addAttribute("daysToRenew", Common.DAYS_TO_RENEW);

		model.addAttribute("previousArtifact", artifactQuery);
		model.addAttribute("previousMember", memberQuery);
		model.addAttribute("previousFromDate", fromDate);
		model.addAttribute("previousToDate", toDate);
		model.addAttribute("previousType", dateType);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "admin/loan/view.html";
	}

	@PostMapping("/admin/loans/return")
	@ResponseBody
	public String loansReturn(@RequestParam(name = "id") String stringId, Model model) {
		return loanHistoryService.returnn(stringId).toString();
	}

	@PostMapping("/admin/loans/renew")
	@ResponseBody
	public String loansRenew(@RequestParam(name = "id") String stringId,
			@RequestParam(required = false) String daysToRenew, Model model) {
		return loanHistoryService.renew(stringId, daysToRenew).toString();
	}

	@PostMapping("/admin/loans/lost")
	@ResponseBody
	public String loansLost(@RequestParam(name = "id") String stringId, Model model) {
		return loanHistoryService.lost(stringId).toString();
	}

	@PostMapping("/admin/loans/delete")
	@ResponseBody
	public String loansDelete(@RequestParam(name = "id") String stringId, Model model) {
		return loanHistoryService.delete(stringId).toString();
	}

	@GetMapping("/admin/loans/edit")
	public String loansEdit(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String artifactQuery,
			@RequestParam(defaultValue = "", required = false) String memberQuery,
			@RequestParam(defaultValue = "", required = false) String fromDate,
			@RequestParam(defaultValue = "", required = false) String toDate,
			@RequestParam(defaultValue = "", required = false) String dateType, Model model) {
		Page<LoanHistory> loans = loanHistoryService.searchAllButLost(artifactQuery, memberQuery, fromDate, toDate,
				dateType, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
		model.addAttribute("totalPages", loans.getTotalPages());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("loans", loans);

		model.addAttribute("previousArtifact", artifactQuery);
		model.addAttribute("previousMember", memberQuery);
		model.addAttribute("previousFromDate", fromDate);
		model.addAttribute("previousToDate", toDate);
		model.addAttribute("previousType", dateType);
		return "admin/loan/view.html";
	}

	@GetMapping("/admin/losts/view")
	public String lostsView(Model model) {
		return "admin/lost/view.html";
	}

	@GetMapping("/admin/reserves/view")
	public String reservesView(Model model) {
		return "admin/reserve/view.html";
	}
}