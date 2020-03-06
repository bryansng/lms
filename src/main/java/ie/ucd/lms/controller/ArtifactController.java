package ie.ucd.lms.controller;

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
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.ArtifactService;
import ie.ucd.lms.service.Common;

@Controller
public class ArtifactController {
  @Autowired
  ArtifactRepository artifactRepository;

  @Autowired
  ArtifactService artifactService;

  @GetMapping("/artifacts/view")
  public String artifactViewOne(@RequestParam(name = "id") String stringId, Model model) {
    Artifact artifact = artifactRepository.findById(Common.convertStringToLong(stringId)).get();
    String publishedOn = artifact.getPublishedOn().format(Common.dateFormatter);
    model.addAttribute("artifact", artifact);
    model.addAttribute("publishedOn", publishedOn);
    return "artifact.html";
  }

  @GetMapping("/admin/artifacts/view")
  public String artifactsView(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String searchQuery,
      @RequestParam(defaultValue = "", required = false) String type,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    Page<Artifact> artifacts = artifactService.search(searchQuery, type, page - 1);
    model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
    model.addAttribute("totalPages", artifacts.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("artifacts", artifacts);

    model.addAttribute("previousQuery", searchQuery);
    model.addAttribute("previousType", type);
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    return "admin/artifact/view.html";
  }

  @GetMapping("/admin/artifacts/edit")
  public String artifactsEditGet(@RequestParam(name = "id") String stringId, Model model) {
    Artifact artifact = artifactRepository.findById(Common.convertStringToLong(stringId)).get();
    String publishedOn = artifact.getPublishedOn().format(Common.dateFormatter);
    model.addAttribute("artifact", artifact);
    model.addAttribute("publishedOn", publishedOn);
    return "admin/artifact/edit.html";
  }

  @PostMapping("admin/artifacts/edit")
  public String artifactsEditPost(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(name = "id") String stringId, @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "type", required = true) String type,
      @RequestParam(defaultValue = "", required = false) String genre,
      @RequestParam(name = "authors", required = false) String authors,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(defaultValue = "", required = false) String subtitle,
      @RequestParam(name = "description", required = false) String description,
      @RequestParam(name = "publishers", required = false) String publishers,
      @RequestParam(name = "publishedOn", required = true) String publishedOn,
      @RequestParam(name = "itemPrice", required = false) String itemPrice,
      @RequestParam(name = "quantity", required = true) String quantity,
      @RequestParam(name = "totalQuantity", required = true) String totalQuantity,
      @RequestParam(name = "rackLocation", required = false) String rackLocation,
      @RequestParam(name = "thumbnailLink", required = false) String thumbnailLink,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    ActionConclusion actionConclusion = artifactService.update(stringId, isbn, type, genre, authors, title, subtitle,
        description, publishers, publishedOn, itemPrice, quantity, totalQuantity, rackLocation, thumbnailLink);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<Artifact> artifacts = artifactService.search("", type, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
      model.addAttribute("totalPages", artifacts.getTotalPages());
      model.addAttribute("currentPage", page + 1);
      model.addAttribute("artifacts", artifacts);

      model.addAttribute("previousQuery", "");
      model.addAttribute("previousType", type);
      return "admin/artifact/view.html";
    } else {
      Artifact artifact = artifactRepository.findById(Common.convertStringToLong(stringId)).get();
      model.addAttribute("artifact", artifact);
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousType", type);
      model.addAttribute("previousGenre", genre);
      model.addAttribute("previouseAuthors", authors);
      model.addAttribute("previouseTitle", title);
      model.addAttribute("previousDescription", description);
      model.addAttribute("previousPublishers", publishers);
      model.addAttribute("previousPublishedOn", publishedOn);
      model.addAttribute("previousItemPrice", itemPrice);
      model.addAttribute("previousQuantity", quantity);
      model.addAttribute("previousTotalQuantity", totalQuantity);
      model.addAttribute("previousRackLocation", rackLocation);
      model.addAttribute("previousThumbnailLink", thumbnailLink);
      return "admin/artifact/edit.html";
    }
  }

  @GetMapping("/admin/artifacts/create")
  public String artifactsCreateGet() {
    return "admin/artifact/create.html";
  }

  @PostMapping("admin/artifacts/create")
  public String artifactsCreatePost(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "type", required = true) String type,
      @RequestParam(defaultValue = "", required = false) String genre,
      @RequestParam(name = "authors", required = false) String authors,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(defaultValue = "", required = false) String subtitle,
      @RequestParam(name = "description", required = false) String description,
      @RequestParam(name = "publishers", required = false) String publishers,
      @RequestParam(name = "publishedOn", defaultValue = "1990-01-01", required = true) String publishedOn,
      @RequestParam(name = "itemPrice", required = false) String itemPrice,
      @RequestParam(name = "quantity", defaultValue = "1", required = true) String quantity,
      @RequestParam(name = "totalQuantity", defaultValue = "1", required = true) String totalQuantity,
      @RequestParam(name = "rackLocation", required = false) String rackLocation,
      @RequestParam(name = "thumbnailLink", required = false) String thumbnailLink,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    publishedOn = publishedOn.length() == 4 ? publishedOn.concat("-01-01") : publishedOn;
    ActionConclusion actionConclusion = artifactService.create(isbn, type, genre, authors, title, subtitle, description,
        publishers, publishedOn, itemPrice, quantity, totalQuantity, rackLocation, thumbnailLink);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<Artifact> artifacts = artifactService.search("", type, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - artifacts.getTotalElements());
      model.addAttribute("totalPages", artifacts.getTotalPages());
      model.addAttribute("currentPage", page + 1);
      model.addAttribute("artifacts", artifacts);

      model.addAttribute("previousQuery", "");
      model.addAttribute("previousType", type);
      return "admin/artifact/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousType", type);
      model.addAttribute("previousGenre", genre);
      model.addAttribute("previouseAuthors", authors);
      model.addAttribute("previouseTitle", title);
      model.addAttribute("previousDescription", description);
      model.addAttribute("previousPublishers", publishers);
      model.addAttribute("previousPublishedOn", publishedOn);
      model.addAttribute("previousItemPrice", itemPrice);
      model.addAttribute("previousQuantity", quantity);
      model.addAttribute("previousTotalQuantity", totalQuantity);
      model.addAttribute("previousRackLocation", rackLocation);
      model.addAttribute("previousThumbnailLink", thumbnailLink);
      return "admin/artifact/create.html";
    }
  }

  @PostMapping("/admin/artifacts/delete")
  @ResponseBody
  public ActionConclusion artifactsDelete(@RequestParam(name = "id") String stringId, Model model) {
    return artifactService.delete(stringId);
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
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    // if searchQuery empty, dont allow search and return nothing?
    Page<Artifact> artifacts = artifactService.search(searchQuery, "", page - 1, Common.PAGINATION_ROWS);
    model.addAttribute("totalElements", artifacts.getTotalElements());
    model.addAttribute("totalPages", artifacts.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("artifacts", artifacts);

    model.addAttribute("previousSearchQuery", searchQuery);
    model.addAttribute("previousType", type);
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    return "search.html";
  }
}