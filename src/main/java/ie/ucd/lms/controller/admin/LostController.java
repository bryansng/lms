package ie.ucd.lms.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import ie.ucd.lms.dao.LoanHistoryRepository;
import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.LoanHistoryService;

@Controller
public class LostController {
  @Autowired
  LoanHistoryService loanHistoryService;

  @Autowired
  LoanHistoryRepository loanHistoryRepository;

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
    model.addAttribute("currentPage", page);
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

  @PostMapping("/admin/losts/edit")
  public String lostsEditPost(@RequestParam(name = "id") String stringId,
      @RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "artifactID", required = false) String artifactID,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "issuedOn", required = true) String issuedOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String updateStatus,
      @RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
    if (loanHistoryService.update(stringId, isbn, memberID, issuedOn, "", fine, status)) {
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
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousIssuedOn", issuedOn);
      model.addAttribute("previousFine", fine);
      model.addAttribute("previousUpdateStatus", "fail");
      model.addAttribute("previousUpdateMessage", "");
      model.addAttribute("previousErrorMessage", "Failed to Edit Lost. Please try again.");
      return "admin/lost/edit.html";
    }
  }

  @PostMapping("/admin/losts/create")
  public String lostsCreatePost(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "artifactID", required = false) String artifactID,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "issuedOn", required = true) String issuedOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String updateStatus,
      @RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
    System.out.println(isbn);
    System.out.println(memberID);
    System.out.println(issuedOn);
    System.out.println(fine);
    System.out.println(status);
    if (loanHistoryService.create(isbn, memberID, issuedOn, "", fine, status)) {
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
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousIssuedOn", issuedOn);
      model.addAttribute("previousFine", fine);
      model.addAttribute("previousUpdateStatus", "fail");
      model.addAttribute("previousUpdateMessage", "");
      model.addAttribute("previousErrorMessage", "Failed to Create Lost. Please try again.");
      return "admin/lost/create.html";
    }
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

  @GetMapping("/admin/losts/create")
  public String lostsCreateGet(Model model) {
    String issuedOn = LocalDate.now().format(Common.dateFormatter);
    model.addAttribute("issuedOn", issuedOn);
    return "admin/lost/create.html";
  }

  @GetMapping("/admin/losts/edit")
  public String loansEditGet(@RequestParam(name = "id") String stringId, Model model) {
    LoanHistory loan = loanHistoryRepository.getOne(Common.convertStringToLong(stringId));
    model.addAttribute("loan", loan);
    model.addAttribute("title", loan.getArtifact().getTitle());
    model.addAttribute("issuedOn", loan.getIssuedOn().format(Common.dateFormatter));
    return "admin/lost/edit.html";
  }
}