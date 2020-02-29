package ie.ucd.lms.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.Common;

@Controller
public class ArtifactController {
	@Autowired
	ArtifactRepository artifactRepository;

	@Autowired
	ArtifactService artifactService;

	@GetMapping("/admin/artifacts/view")
	public String artifactsView(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String type,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		Page<Artifact> artifacts = artifactService.search(searchQuery, type, page - 1);
		model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
		model.addAttribute("totalPages", artifacts.getTotalPages());
		model.addAttribute("currentPage", page);
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

	@GetMapping("/artifacts/search")
	@ResponseBody
	public Page<Artifact> artifactsSearch(@RequestParam(defaultValue = "", required = false) String searchQuery) {
		return artifactService.search(searchQuery, "", 0, Common.QUICK_SEARCH_ROWS);
	}

	@GetMapping("/search")
	public String searchPage(@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(defaultValue = "", required = false) String searchQuery,
			@RequestParam(defaultValue = "", required = false) String type,
			@RequestParam(defaultValue = "", required = false) String updateStatus,
			@RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
		// if searchQuery empty, dont allow search and return nothing?
		Page<Artifact> artifacts = artifactService.search(searchQuery, "", page - 1, Common.PAGINATION_ROWS);
		model.addAttribute("totalElements", artifacts.getTotalElements());
		model.addAttribute("totalPages", artifacts.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("artifacts", artifacts);

		model.addAttribute("previousSearchQuery", searchQuery);
		model.addAttribute("previousType", type);
		model.addAttribute("previousUpdateStatus", updateStatus);
		model.addAttribute("previousErrorMessage", errorMessage);
		return "search.html";
	}
}