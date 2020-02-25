package ie.ucd.lms.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import ie.ucd.lms.service.admin.ReserveQueueService;

@Controller
public class ReserveController {
	@Autowired
	ReserveQueueService reserveQueueService;

	@GetMapping("/admin/reserves/view")
	public String reservesView(Model model) {
		return "admin/reserve/view.html";
	}
}