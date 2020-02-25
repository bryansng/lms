package ie.ucd.lms.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.LoanHistoryService;

@Controller
public class LoanController {
	@Autowired
	LoanHistoryService loanHistoryService;

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
}