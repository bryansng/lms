package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import ie.ucd.lms.service.ReportService;

@Controller
public class OtherController {
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
}