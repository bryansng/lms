package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.Common;

@Controller
public class DefaultController {
  @Autowired
  ArtifactRepository artifactRepository;

  @Autowired
  ArtifactService artifactService;

  @GetMapping("/admin/artifacts/search")
  public String artifactsView(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "searchQuery", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String type, Model model) {
    Page<Artifact> artifacts = artifactService.search(artifactQuery, type, page - 1);
    model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
    model.addAttribute("totalPages", artifacts.getTotalPages());
    model.addAttribute("currentPage", page + 1);
    model.addAttribute("artifacts", artifacts);

    model.addAttribute("previousQuery", artifactQuery);
    model.addAttribute("previousType", type);
    return "admin/artifact/view.html";
  }

  @GetMapping("/")
  public String indexView() {
    return "index.html";
  }
}