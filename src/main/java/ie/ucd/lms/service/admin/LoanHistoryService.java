package ie.ucd.lms.service.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.LoanHistoryRepository;
import ie.ucd.lms.dao.ReserveQueueRepository;
import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.LoanHistory;

@Service
public class LoanHistoryService {
  @Autowired
  LoanHistoryRepository loanHistoryRepository;

  @Autowired
  ReserveQueueRepository reserveQueueRepository;

  @Autowired
  ArtifactRepository artifactRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ReserveQueueService reserveQueueService;

  public Page<LoanHistory> searchAll(String artifact, String member, String fromDate, String toDate, String status,
      int pageNum) {
    Long artifactId = Common.convertStringToLong(artifact);
    Long memberId = Common.convertStringToLong(member);
    LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
    LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
    PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

    Page<LoanHistory> res = loanHistoryRepository.findAllByArtifactAndMemberAndBothDatesAndStatus(artifactId, artifact,
        memberId, member, fromDateTime, toDateTime, status, pRequest);
    return res;
  }

  public Page<LoanHistory> searchAllButLost(String artifact, String member, String fromDate, String toDate,
      String dateType, int pageNum) {
    Long artifactId = Common.convertStringToLong(artifact);
    Long memberId = Common.convertStringToLong(member);
    LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
    LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
    PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

    switch (dateType.toLowerCase()) {
      case "issued date":
        return loanHistoryRepository.findAllByArtifactAndMemberAndIssuedDateAndStatusNot(artifactId, artifact, memberId,
            member, fromDateTime, toDateTime, "lost", pRequest);
      case "return date":
        return loanHistoryRepository.findAllByArtifactAndMemberAndReturnDateAndStatusNot(artifactId, artifact, memberId,
            member, fromDateTime, toDateTime, "lost", pRequest);
      default:
        return loanHistoryRepository.findAllByArtifactAndMemberAndBothDatesAndStatusNot(artifactId, artifact, memberId,
            member, fromDateTime, toDateTime, "lost", pRequest);
    }
  }

  public Page<LoanHistory> searchLost(String artifact, String member, String fromDate, String toDate, int pageNum) {
    Long artifactId = Common.convertStringToLong(artifact);
    Long memberId = Common.convertStringToLong(member);
    LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
    LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
    PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

    Page<LoanHistory> res = loanHistoryRepository.findAllByArtifactAndMemberAndIssuedDateAndStatus(artifactId, artifact,
        memberId, member, fromDateTime, toDateTime, "lost", pRequest);
    return res;
  }

  public Boolean update(String stringId, String isbn, String memberId, String issuedOn, String returnOn, String fine,
      String status) {
    Long id = Common.convertStringToLong(stringId);
    Long aMemberId = Common.convertStringToLong(memberId);

    if (issuedOn.equals("") && !returnOn.equals("")) {
      issuedOn = LocalDateTime.now().format(Common.dateFormatter);
    } else if (!issuedOn.equals("") && returnOn.equals("")) {
      returnOn = issuedOn;
    }

    if (loanHistoryRepository.existsById(id)) {
      if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
        Artifact artifact = artifactRepository.findByIsbn(isbn);
        Member member = memberRepository.getOne(aMemberId);
        LoanHistory loanHistory = loanHistoryRepository.getOne(id);
        loanHistory.setAll(isbn, memberId, issuedOn, returnOn, fine, status, artifact, member);
        loanHistoryRepository.save(loanHistory);
        return true;
      }
    }
    return false;
  }

  /**
   * @return false if artifact not in stock, and reserveQueue already has user with same isbn and memberId, or id and isbn already exists.
   */
  public Boolean create(String isbn, String memberId, String issuedOn, String returnOn, String fine, String status) {
    Long aMemberId = Common.convertStringToLong(memberId);

    if (issuedOn.equals("") && !returnOn.equals("")) {
      issuedOn = LocalDateTime.now().format(Common.dateFormatter);
    } else if (!issuedOn.equals("") && returnOn.equals("")) {
      returnOn = issuedOn;
    }

    // check by loan id, isbn, memberId

    // if (!loanHistoryRepository.existsByIsbnAndMemberId(isbn, aMemberId)) {
    if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
      Artifact artifact = artifactRepository.findByIsbn(isbn);
      Member member = memberRepository.getOne(aMemberId);
      if (artifact.inStock()) {
        artifact.decrementQuantity();
        LoanHistory loanHistory = new LoanHistory();
        loanHistory.setAll(isbn, memberId, issuedOn, returnOn, fine, status, artifact, member);
        loanHistoryRepository.save(loanHistory);
        return true;
      } else {
        return reserveQueueService.create(isbn, memberId, Common.DEFAULT_EXPIRED_ON);
      }
    }
    // }
    return false;
  }

  /**
   * @return false if id does not exist.
   */
  public Boolean delete(String stringId) {
    Long id = Common.convertStringToLong(stringId);

    if (loanHistoryRepository.existsById(id)) {
      handleArtifactStock(loanHistoryRepository.getOne(id));
      loanHistoryRepository.deleteById(id);
      return true;
    }
    return false;
  }

  /**
   * @return false if id does not exist.
   */
  public Boolean restocked(String stringId) {
    Long id = Common.convertStringToLong(stringId);

    if (loanHistoryRepository.existsById(id)) {
      LoanHistory loanHistory = loanHistoryRepository.getOne(id);
      loanHistory.setStatus("restocked");

      Artifact artifact = loanHistory.getArtifact();
      // artifact.decrementQuantity(); // because lost() does not increment, so system still assumes the book is issued, but is actually lost. So we are actually derementing the issued quantity.
      artifact.incrementQuantity();
      loanHistoryRepository.save(loanHistory);
      return true;
    }
    return false;
  }

  /**
   * @return false if id does not exist.
   */
  public Boolean returnn(String stringId) {
    Long id = Common.convertStringToLong(stringId);

    if (loanHistoryRepository.existsById(id)) {
      LoanHistory loanHistory = loanHistoryRepository.getOne(id);
      loanHistory.setReturnedOn(LocalDateTime.now());
      loanHistory.setStatus("returned");

      Artifact artifact = loanHistory.getArtifact();
      artifact.incrementQuantity();
      loanHistoryRepository.save(loanHistory);
      return true;
    }
    return false;
  }

  /**
   * @return false if exists in reserveQueue, or id does not exist.
   */
  public Boolean renew(String stringId, String daysToRenew) {
    Long id = Common.convertStringToLong(stringId);
    Long days = Common.convertStringToLong(daysToRenew);

    if (loanHistoryRepository.existsById(id)) {
      LoanHistory loanHistory = loanHistoryRepository.getOne(id);
      if (reserveQueueRepository.existsByIsbn(loanHistory.getIsbn())) {
        // returnn(stringId); // ? why did I put return here again? (assumed they renew in person, if so, they cant renew if is reserved, so return the book, and set up reservation if required)
        // ? maybe add isbn + member_id to reserve list?
        // ? prompt librarian / member?
        return false;
      }
      loanHistory.setReturnOn(LocalDateTime.now().plusDays(days));
      loanHistory.setStatus("renewed");
      loanHistoryRepository.save(loanHistory);
      return true;
    }
    return false;
  }

  /**
   * @return false if id does not exist.
   */
  public Boolean lost(String stringId) {
    Long id = Common.convertStringToLong(stringId);

    if (loanHistoryRepository.existsById(id)) {
      LoanHistory loanHistory = loanHistoryRepository.getOne(id);
      loanHistory.setFine(loanHistory.getFine().add(loanHistory.getArtifact().getItemPrice()));
      loanHistory.setFinedOn(LocalDateTime.now());
      loanHistory.setLostOn(LocalDateTime.now());
      loanHistory.setStatus("lost");
      loanHistory.setWasLost(true);
      loanHistoryRepository.save(loanHistory);
      return true;
    }
    return false;
  }

  /**
   * A book cant just disappear if we delete an entry/a row.
   * Significant if librarian created loan or whatever accidentally
   * and wants to delete it, only to cause the artifact stock to
   * be wrong for some reason.
   *
   * This function basically checks the loan status,
   * depending on that status, add it back.
   * @param loanHistory
   */
  private void handleArtifactStock(LoanHistory loanHistory) {
    String status = loanHistory.getStatus();
    System.out.println(loanHistory);
    if (status.equals("issued") || status.equals("renewed") || status.equals("delayed") || status.equals("lost")) {
      loanHistory.getArtifact().incrementQuantity();
      System.out.println("incremented");
    }
  }

  private void printMe(List<LoanHistory> arr) {
    System.out.println("\n\nPrinting search result:");
    for (LoanHistory loanHistory : arr) {
      System.out.println(loanHistory);
    }
    ;
  }

  public void printAll() {
    System.out.println("\n\nPrinting search result:");
    for (LoanHistory loanHistory : loanHistoryRepository.findAll()) {
      System.out.println(loanHistory);
    }
    ;
  }
}