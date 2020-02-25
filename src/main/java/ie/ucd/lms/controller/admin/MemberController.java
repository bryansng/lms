package ie.ucd.lms.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.MemberService;

@Controller
public class MemberController {
	@Autowired
	MemberService memberService;

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
}