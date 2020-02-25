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
public class LostController {
	@Autowired
	LoanHistoryService loanHistoryService;

	@GetMapping("/admin/losts/view")
	public String lostsView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String artifactQuery,
			@RequestParam(defaultValue = "", required = false) String memberQuery,
			@RequestParam(defaultValue = "", required = false) String fromDate,
			@RequestParam(defaultValue = "", required = false) String toDate,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<LoanHistory> loans = loanHistoryService.searchLost(artifactQuery, memberQuery, fromDate, toDate, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
		model.addAttribute("totalPages", loans.getTotalPages());
		model.addAttribute("currentPage", page + 1);
		model.addAttribute("loans", loans);
		model.addAttribute("daysToRenew", Common.DAYS_TO_RENEW);

		model.addAttribute("previousArtifact", artifactQuery);
		model.addAttribute("previousMember", memberQuery);
		model.addAttribute("previousFromDate", fromDate);
		model.addAttribute("previousToDate", toDate);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "admin/lost/view.html";
	}

	@PostMapping("/admin/losts/restock")
	@ResponseBody
	public String lostsLost(@RequestParam(name = "id") String stringId, Model model) {
		return loanHistoryService.restocked(stringId).toString();
	}

	@PostMapping("/admin/losts/delete")
	@ResponseBody
	public String lostsDelete(@RequestParam(name = "id") String stringId, Model model) {
		return loanHistoryService.delete(stringId).toString();
	}
}