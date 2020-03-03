package ie.ucd.lms.controller;

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
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.Common;
import ie.ucd.lms.service.LoanHistoryService;

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
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
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
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    return "admin/lost/view.html";
  }

  @GetMapping("/admin/losts/edit")
  public String loansEditGet(@RequestParam(name = "id") String stringId, Model model) {
    LoanHistory loan = loanHistoryRepository.getOne(Common.convertStringToLong(stringId));
    model.addAttribute("loan", loan);
    model.addAttribute("title", loan.getArtifact().getTitle());
    model.addAttribute("issuedOn", loan.getIssuedOn().format(Common.dateFormatter));
    return "admin/lost/edit.html";
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
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    ActionConclusion actionConclusion = loanHistoryService.update(stringId, isbn, memberID, issuedOn, "", fine, status);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
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
      return "admin/lost/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousIssuedOn", issuedOn);
      model.addAttribute("previousFine", fine);
      return "admin/lost/edit.html";
    }
  }

  @GetMapping("/admin/losts/create")
  public String lostsCreateGet(Model model) {
    String issuedOn = LocalDate.now().format(Common.dateFormatter);
    model.addAttribute("issuedOn", issuedOn);
    return "admin/lost/create.html";
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
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model) {
    ActionConclusion actionConclusion = loanHistoryService.create(isbn, memberID, issuedOn, "", fine, status);
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    if (actionConclusion.isSuccess) {
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
      return "admin/lost/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousIssuedOn", issuedOn);
      model.addAttribute("previousFine", fine);
      return "admin/lost/create.html";
    }
  }

  @PostMapping("/admin/losts/restock")
  @ResponseBody
  public ActionConclusion lostsRestock(@RequestParam(name = "id") String stringId, Model model) {
    return loanHistoryService.restocked(stringId);
  }

  @PostMapping("/admin/losts/delete")
  @ResponseBody
  public ActionConclusion lostsDelete(@RequestParam(name = "id") String stringId, Model model) {
    return loanHistoryService.delete(stringId);
  }
}