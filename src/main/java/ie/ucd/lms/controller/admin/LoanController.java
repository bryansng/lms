package ie.ucd.lms.controller.admin;

import java.time.LocalDate;
import java.util.Date;

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
public class LoanController {
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
      @RequestParam(defaultValue = "", required = false) String updateStatus,
      @RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
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
    model.addAttribute("previousUpdateStatus", updateStatus);
    model.addAttribute("previousErrorMessage", errorMessage);
    return "admin/loan/view.html";
  }

  @GetMapping("/admin/loans/create")
  public String loansCreateGet(Model model) {
    String returnOn = LocalDate.now().plusDays(7).format(Common.dateFormatter);
    model.addAttribute("returnOn", returnOn);
    return "admin/loan/create.html";
  }

  @GetMapping("/admin/loans/edit")
  public String loansEditGet(@RequestParam(name = "id") String stringId, Model model) {
    LoanHistory loan = loanHistoryRepository.getOne(Common.convertStringToLong(stringId));
    model.addAttribute("loan", loan);
    model.addAttribute("title", loan.getArtifact().getTitle());
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
      @RequestParam(name = "artifactID", required = false) String artifactID,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", defaultValue = "0.00", required = false) String status,
      @RequestParam(name = "returnOn", required = true) String returnOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String updateStatus,
      @RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
    if (loanHistoryService.update(stringId, isbn, memberID, returnOn, fine, status)) {
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
      model.addAttribute("previousUpdateStatus", "success");
      model.addAttribute("previousUpdateMessage", "Updated Loan Succesfully.");
      model.addAttribute("previousErrorMessage", "");
      return "admin/loan/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousReturnOn", returnOn);
      model.addAttribute("previousFine", fine);
      model.addAttribute("previousUpdateStatus", "fail");
      model.addAttribute("previousUpdateMessage", "");
      model.addAttribute("previousErrorMessage", "Failed to Update Loan. Please try again.");
      return "admin/loan/create.html";
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
      @RequestParam(name = "artifactID", required = false) String artifactID,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", defaultValue = "0.00", required = false) String status,
      @RequestParam(name = "returnOn", required = true) String returnOn,
      @RequestParam(name = "fine", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String updateStatus,
      @RequestParam(defaultValue = "", required = false) String errorMessage, Model model) {
    if (loanHistoryService.create(isbn, memberID, returnOn, fine, status)) {
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
      model.addAttribute("previousUpdateStatus", "success");
      model.addAttribute("previousUpdateMessage", "Created Loan Succesfully.");
      model.addAttribute("previousErrorMessage", "");
      return "admin/loan/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousID", artifactID);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousStatus", status);
      model.addAttribute("previousReturnOn", returnOn);
      model.addAttribute("previousFine", fine);
      model.addAttribute("previousUpdateStatus", "fail");
      model.addAttribute("previousUpdateMessage", "");
      model.addAttribute("previousErrorMessage", "Failed to Create Loan. Please try again.");
      return "admin/loan/create.html";
    }
  }

  @PostMapping("/admin/loans/return")
  @ResponseBody
  public String loansReturn(@RequestParam(name = "id") String stringId, Model model) {
    return loanHistoryService.returnn(stringId).toString();
  }

  @PostMapping("/admin/loans/renew")
  @ResponseBody
  public String loansRenew(@RequestParam(name = "id") String stringId,
      @RequestParam(required = false) String daysToRenew, Model model) {
    return loanHistoryService.renew(stringId, daysToRenew).toString();
  }

  @PostMapping("/admin/loans/lost")
  @ResponseBody
  public String loansLost(@RequestParam(name = "id") String stringId, Model model) {
    return loanHistoryService.lost(stringId).toString();
  }

  @PostMapping("/admin/loans/delete")
  @ResponseBody
  public String loansDelete(@RequestParam(name = "id") String stringId, Model model) {
    return loanHistoryService.delete(stringId).toString();
  }
}