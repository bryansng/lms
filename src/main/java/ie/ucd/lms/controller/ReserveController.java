package ie.ucd.lms.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import ie.ucd.lms.dao.ReserveQueueRepository;
import ie.ucd.lms.entity.ReserveQueue;
import ie.ucd.lms.service.ActionConclusion;
import ie.ucd.lms.service.Common;
import ie.ucd.lms.service.ReserveQueueService;
import ie.ucd.lms.service.LoginService;
import org.springframework.security.core.Authentication;

@Controller
public class ReserveController {
  @Autowired
  LoginService loginService;

  @Autowired
  ReserveQueueService reserveQueueService;

  @Autowired
  ReserveQueueRepository reserveQueueRepository;

  @GetMapping("/admin/reserves/view")
  public String reservesView(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    Page<ReserveQueue> reserves = reserveQueueService.search(artifactQuery, memberQuery, fromDate, toDate, page - 1);
    Map<Long, Long> positionInQueue = reserveQueueService.searchFirstInQueueByArtifact(artifactQuery, memberQuery,
        fromDate, toDate, page - 1);
    // reserveQueueService.nextInLine(isbn);
    model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - reserves.getTotalElements());
    model.addAttribute("totalPages", reserves.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("reserves", reserves);
    model.addAttribute("positionInQueue", positionInQueue);
    model.addAttribute("daysToLoan", Common.DAYS_TO_LOAN);

    model.addAttribute("previousArtifact", artifactQuery);
    model.addAttribute("previousMember", memberQuery);
    model.addAttribute("previousFromDate", fromDate);
    model.addAttribute("previousToDate", toDate);
    model.addAttribute("previousIsSuccess", isSuccess);
    model.addAttribute("previousSuccessMessage", successMessage);
    model.addAttribute("previousFailureMessage", failureMessage);
    loginService.addMemberToModel(model, authentication);
    return "admin/reserve/view.html";
  }

  @GetMapping("/admin/reserves/create")
  public String reservesCreateGet(Model model, Authentication authentication) {
    model.addAttribute("expiredOn", LocalDate.now().plusDays(7).format(Common.dateFormatter));
    loginService.addMemberToModel(model, authentication);
    return "admin/reserve/create.html";
  }

  @GetMapping("/admin/reserves/edit")
  public String reservesEditGet(@RequestParam(name = "id") String stringId, Model model,
      Authentication authentication) {
    ReserveQueue reserve = reserveQueueRepository.findById(Common.convertStringToLong(stringId)).get();
    model.addAttribute("reserve", reserve);
    model.addAttribute("expiredOn", reserve.getExpiredOn().format(Common.dateFormatter));
    loginService.addMemberToModel(model, authentication);
    return "admin/reserve/edit.html";
  }

  @PostMapping("/admin/reserves/create")
  public String reservesCreatePost(@RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", defaultValue = "", required = false) String title,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", defaultValue = "", required = false) String status,
      @RequestParam(name = "expiredOn", required = true) String expiredOn,
      @RequestParam(name = "fine", defaultValue = "0.00", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    ActionConclusion actionConclusion = reserveQueueService.create(isbn, memberID, expiredOn);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<ReserveQueue> reserves = reserveQueueService.search(artifactQuery, memberQuery, fromDate, toDate, page - 1);
      Map<Long, Long> positionInQueue = reserveQueueService.searchFirstInQueueByArtifact(artifactQuery, memberQuery,
          fromDate, toDate, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - reserves.getTotalElements());
      model.addAttribute("totalPages", reserves.getTotalPages());
      model.addAttribute("currentPage", page);
      model.addAttribute("reserves", reserves);
      model.addAttribute("positionInQueue", positionInQueue);
      model.addAttribute("daysToLoan", Common.DAYS_TO_LOAN);

      model.addAttribute("previousArtifact", artifactQuery);
      model.addAttribute("previousMember", memberQuery);
      model.addAttribute("previousFromDate", fromDate);
      model.addAttribute("previousToDate", toDate);
      return "admin/reserve/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousExpiredOn", expiredOn);
      return "admin/reserve/create.html";
    }
  }

  @PostMapping("/admin/reserves/edit")
  public String reservesEditPost(@RequestParam(name = "id") String stringId,
      @RequestParam(defaultValue = "1", required = false) Integer page,
      @RequestParam(defaultValue = "", required = false) String artifactQuery,
      @RequestParam(defaultValue = "", required = false) String memberQuery,
      @RequestParam(defaultValue = "", required = false) String fromDate,
      @RequestParam(defaultValue = "", required = false) String toDate,
      @RequestParam(defaultValue = "", required = false) String dateType,
      @RequestParam(name = "isbn", required = true) String isbn,
      @RequestParam(name = "title", defaultValue = "", required = false) String title,
      @RequestParam(name = "memberID", required = true) String memberID,
      @RequestParam(name = "status", defaultValue = "", required = false) String status,
      @RequestParam(name = "expiredOn", required = true) String expiredOn,
      @RequestParam(name = "fine", defaultValue = "0.00", required = false) String fine,
      @RequestParam(defaultValue = "", required = false) String isSuccess,
      @RequestParam(defaultValue = "", required = false) String successMessage,
      @RequestParam(defaultValue = "", required = false) String failureMessage, Model model,
      Authentication authentication) {
    loginService.addMemberToModel(model, authentication);
    ActionConclusion actionConclusion = reserveQueueService.update(stringId, isbn, memberID, expiredOn);
    model.addAttribute("previousIsSuccess", actionConclusion.isSuccess.toString());
    model.addAttribute("previousSuccessMessage", actionConclusion.message);
    model.addAttribute("previousFailureMessage", actionConclusion.message);
    if (actionConclusion.isSuccess) {
      Page<ReserveQueue> reserves = reserveQueueService.search(artifactQuery, memberQuery, fromDate, toDate, page - 1);
      Map<Long, Long> positionInQueue = reserveQueueService.searchFirstInQueueByArtifact(artifactQuery, memberQuery,
          fromDate, toDate, page - 1);
      model.addAttribute("totalEmptyRows", Common.PAGINATION_ROWS - reserves.getTotalElements());
      model.addAttribute("totalPages", reserves.getTotalPages());
      model.addAttribute("currentPage", page);
      model.addAttribute("reserves", reserves);
      model.addAttribute("positionInQueue", positionInQueue);
      model.addAttribute("daysToLoan", Common.DAYS_TO_LOAN);

      model.addAttribute("previousArtifact", artifactQuery);
      model.addAttribute("previousMember", memberQuery);
      model.addAttribute("previousFromDate", fromDate);
      model.addAttribute("previousToDate", toDate);
      return "admin/reserve/view.html";
    } else {
      model.addAttribute("previousISBN", isbn);
      model.addAttribute("previousTitle", title);
      model.addAttribute("previousMemberID", memberID);
      model.addAttribute("previousExpiredOn", expiredOn);
      return "admin/reserve/edit.html";
    }
  }

  @PostMapping("/admin/reserves/loan")
  @ResponseBody
  public ActionConclusion reservesLoan(@RequestParam(name = "id") String stringId,
      @RequestParam(required = false) String daysToLoan) {
    return reserveQueueService.loan(stringId, daysToLoan);
  }

  @PostMapping("/admin/reserves/delete")
  @ResponseBody
  public ActionConclusion reservesDelete(@RequestParam(name = "id") String stringId) {
    return reserveQueueService.delete(stringId);
  }
}