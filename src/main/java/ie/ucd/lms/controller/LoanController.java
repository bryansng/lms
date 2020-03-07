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
import ie.ucd.lms.service.LoginService;
import org.springframework.security.core.Authentication;

@Controller
public class LoanController {
  @Autowired
  LoginService loginService;

  @Autowired
  LoanHistoryService loanHistoryService;

  @Autowired
  LoanHistoryRepository loanHistoryRepository;

  @GetMapping("/admin/loans/view")
  public String loansView(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    Page<LoanHistory> loans = loanHistoryService.searchAllButLost(artifactQuery, memberQuery, fromDate, toDate,
        dateType, page - 1);
    model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
    model.addAttribute("totalPages", loans.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("loans", loans);
    model.addAttribute("daysToRenew", Common.DAYS_TO_RENEW);

    model.addAttribute("previousArtifact", artifactQuery);
    model.addAttribute("previousMember", memberQuery);
    model.addAttribute("previousFromDate", fromDate);
    model.addAttribute("previousToDate", toDate);
    model.addAttribute("previousType", dateType);
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    return "admin/loan/view.html";
  }

  @GetMapping("/admin/loans/create")
  public String loansCreateGet(Model model, Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    String returnOn = LocalDate.now().plusDays(7).format(Common.dateFormatter);
    model.addAttribute("returnOn", returnOn);
    return "admin/loan/create.html";
  }

  @GetMapping("/admin/loans/edit")
  public String loansEditGet(@RequestParam(name = "id") String stringId, Model model, Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    LoanHistory loan = loanHistoryRepository.getOne(Common.convertStringToLong(stringId));
    model.addAttribute("loan", loan);
    model.addAttribute("returnOn", loan.getReturnOn().format(Common.dateFormatter));
    return "admin/loan/edit.html";
  }

  @PostMapping("/admin/loans/edit")
  public String loansEditPost(@RequestParam(name = "id") String stringId,
      @RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "returnOn", required = true) String returnOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    ActionConclusion actionConclusion = loanHistoryService.update(stringId, isbn, memberID, "", returnOn, fine, status);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<LoanHistory> loans = loanHistoryService.searchAllButLost(artifactQuery, memberQuery, fromDate, toDate,
          dateType, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
      model.addAttribute("totalPages", loans.getTotalPages());
      model.addAttribute("currentPage", page + 1);
      model.addAttribute("loans", loans);
      model.addAttribute("daysToRenew", Common.DAYS_TO_RENEW);

      model.addAttribute("previousArtifact", artifactQuery);
      model.addAttribute("previousMember", memberQuery);
      model.addAttribute("previousFromDate", fromDate);
      model.addAttribute("previousToDate", toDate);
      model.addAttribute("previousType", dateType);
      return "admin/loan/view.html";
    } else {
      LoanHistory loan = loanHistoryRepository.getOne(Common.convertStringToLong(stringId));
      model.addAttribute("loan", loan);
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousReturnOn", returnOn);
      model.addAttribute("previousFine", fine);
      return "admin/loan/edit.html";
    }
  }

  @PostMapping("/admin/loans/create")
  public String loansCreatePost(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "returnOn", required = true) String returnOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    ActionConclusion actionConclusion = loanHistoryService.create(isbn, memberID, "", returnOn, fine, status);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<LoanHistory> loans = loanHistoryService.searchAllButLost(artifactQuery, memberQuery, fromDate, toDate,
          dateType, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - loans.getTotalElements());
      model.addAttribute("totalPages", loans.getTotalPages());
      model.addAttribute("currentPage", page + 1);
      model.addAttribute("loans", loans);
      model.addAttribute("daysToRenew", Common.DAYS_TO_RENEW);

      model.addAttribute("previousArtifact", artifactQuery);
      model.addAttribute("previousMember", memberQuery);
      model.addAttribute("previousFromDate", fromDate);
      model.addAttribute("previousToDate", toDate);
      model.addAttribute("previousType", dateType);
      return "admin/loan/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousReturnOn", returnOn);
      model.addAttribute("previousFine", fine);
      return "admin/loan/create.html";
    }
  }

  @PostMapping("/admin/loans/return")
  @ResponseBody
  public ActionConclusion loansReturn(@RequestParam(name = "id") String stringId) {
    return loanHistoryService.returnn(stringId);
  }

  @PostMapping("/admin/loans/renew")
  @ResponseBody
  public ActionConclusion loansRenew(@RequestParam(name = "id") String stringId,
      @RequestParam(required = false) String daysToRenew) {
    return loanHistoryService.renew(stringId, daysToRenew);
  }

  @PostMapping("/admin/loans/lost")
  @ResponseBody
  public ActionConclusion loansLost(@RequestParam(name = "id") String stringId) {
    return loanHistoryService.lost(stringId);
  }

  @PostMapping("/admin/loans/delete")
  @ResponseBody
  public ActionConclusion loansDelete(@RequestParam(name = "id") String stringId) {
    return loanHistoryService.delete(stringId);
  }
}