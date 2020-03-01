package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import ie.ucd.lms.entity.ReserveQueue;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.Common;
import ie.ucd.lms.service.ReserveQueueService;

@Controller
public class ReserveController {
	@Autowired
	ReserveQueueService reserveQueueService;

	@GetMapping("/admin/reserves/view")
	public String reservesView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String artifactQuery,
			@RequestParam(defaultValue = "", required = false) String memberQuery,
			@RequestParam(defaultValue = "", required = false) String fromDate,
			@RequestParam(defaultValue = "", required = false) String toDate,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<ReserveQueue> reserves = reserveQueueService.search(artifactQuery, memberQuery, fromDate, toDate, page - 1);
		// reserveQueueService.nextInLine(isbn);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - reserves.getTotalElements());
		model.addAttribute("totalPages", reserves.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("reserves", reserves);
		model.addAttribute("daysToLoan", Common.DAYS_TO_LOAN);

		model.addAttribute("previousArtifact", artifactQuery);
		model.addAttribute("previousMember", memberQuery);
		model.addAttribute("previousFromDate", fromDate);
		model.addAttribute("previousToDate", toDate);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "admin/reserve/view.html";
	}

	@PostMapping("/admin/reserves/loan")
	@ResponseBody
	public ActionConclusion reservesLoan(@RequestParam(name = "id") String stringId,
			@RequestParam(required = false) String daysToLoan, Model model) {
		return reserveQueueService.loan(stringId, daysToLoan);
	}

	@PostMapping("/admin/reserves/delete")
	@ResponseBody
	public String reservesDelete(@RequestParam(name = "id") String stringId, Model model) {
		return reserveQueueService.delete(stringId).toString();
	}
}