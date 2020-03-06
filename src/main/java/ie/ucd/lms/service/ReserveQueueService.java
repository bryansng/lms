package ie.ucd.lms.service;

import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.dao.ReserveQueueRepository;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.ReserveQueue;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReserveQueueService {
  @Autowired
  ReserveQueueRepository reserveQueueRepository;

  @Autowired
  ArtifactRepository artifactRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  LoanHistoryService loanHistoryService;

  public List<ReserveQueue> getReservedLoansForMember(Member member) {
    return reserveQueueRepository.findByMember(member);
  }

  public Page<ReserveQueue> search(String artifact, String member, String fromDate, String toDate, int pageNum) {
    Long artifactId = Common.convertStringToLong(artifact);
    Long memberId = Common.convertStringToLong(member);
    LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
    LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
    PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

    Page<ReserveQueue> res = reserveQueueRepository.findAllByArtifactAndMemberAndBothDatesAndStatus(artifactId,
        artifact, memberId, member, fromDateTime, toDateTime, pRequest);
    // Page<ReserveQueue> res = reserveQueueRepository.findAllByArtifactAndMemberAndBothDatesAndStatus(artifactId,
    //     artifact, memberId, member, fromDateTime, toDateTime, pRequest);
    // Page<ReserveQueue> res = reserveQueueRepository.findByExpiredOnBetween(fromDateTime, toDateTime, pRequest);
    return res;
  }

  public Map<Long, Long> searchFirstInQueueByArtifact(String artifact, String member, String fromDate, String toDate,
      int pageNum) {
    Long artifactId = Common.convertStringToLong(artifact);
    Long memberId = Common.convertStringToLong(member);
    LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
    LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
    PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

    List<Object> artifactIdAndPositionInQueue = reserveQueueRepository.findAllFirstPositionInQueueByArtifact(artifactId,
        artifact, memberId, member, fromDateTime, toDateTime, pRequest).getContent();

    Map<Long, Long> hm = new HashMap<Long, Long>();
    for (Object data : artifactIdAndPositionInQueue) {
      Object[] dataArray = (Object[]) data;
      // String row = "";
      // row += "artifact.id: " + (Long) dataArray[0] + "\t";
      // row += "reserveQueue.id: " + (Long) dataArray[1] + "\n";
      // System.out.println(row);
      hm.put((Long) dataArray[0], (Long) dataArray[1]);
    }
    return hm;
  }

  public ReserveQueue firstInLine(String isbn) {
    return reserveQueueRepository.findFirstByIsbnOrderById(isbn);
  }

  public ActionConclusion update(String stringId, String isbn, String memberId, String expiredOn) {
    Long id = Common.convertStringToLong(stringId);
    Long aMemberId = Common.convertStringToLong(memberId);

    if (reserveQueueRepository.existsById(id)) {
      if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
        Artifact artifact = artifactRepository.findByIsbn(isbn);
        Member member = memberRepository.getOne(aMemberId);
        ReserveQueue reserveQueue = reserveQueueRepository.getOne(id);
        reserveQueue.setAll(isbn, memberId, expiredOn, artifact, member);
        reserveQueueRepository.save(reserveQueue);
        return new ActionConclusion(true, "Updated successfully.");
      }
      return new ActionConclusion(false, "Failed to update. ISBN or member ID does not exist.");
    }
    return new ActionConclusion(false, "Failed to update. Reserve ID does not exist.");
  }

  public ActionConclusion create(String isbn, String memberId, String expiredOn) {
    Long aMemberId = Common.convertStringToLong(memberId);

    if (reserveQueueRepository.countByMemberId(aMemberId) < Common.MAX_RESERVES_PER_USER) {
      // if (!reserveQueueRepository.existsByIsbnAndMemberId(isbn, aMemberId)) {
      if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
        Artifact artifact = artifactRepository.findByIsbn(isbn);
        Member member = memberRepository.getOne(aMemberId);
        ReserveQueue reserveQueue = new ReserveQueue();
        reserveQueue.setAll(isbn, memberId, expiredOn, artifact, member);
        reserveQueueRepository.save(reserveQueue);
        return new ActionConclusion(true, "Created successfully.");
      }
      // }
      return new ActionConclusion(false, "Failed to create. ISBN or member ID does not exist.");
    }
    return new ActionConclusion(false,
        "Unable to create. Member has exceeded the maximum reserve amount: " + Common.MAX_RESERVES_PER_USER);
  }

  public ActionConclusion delete(String stringId) {
    Long id = Common.convertStringToLong(stringId);

    if (reserveQueueRepository.existsById(id)) {
      reserveQueueRepository.deleteById(id);
      return new ActionConclusion(true, "Deleted successfully.");
    }
    return new ActionConclusion(false, "Failed to delete. Reserve ID does not exist.");
  }

  /**
   * @return false if artifact not in stock, or id and isbn already exists.
   */
  public ActionConclusion loan(String stringId, String daysToLoan) {
    Long id = Common.convertStringToLong(stringId);

    if (reserveQueueRepository.existsById(id)) {
      ReserveQueue rQ = reserveQueueRepository.getOne(id);
      Artifact artifact = artifactRepository.findByIsbn(rQ.getIsbn());
      if (artifact.inStock()) {
        if (loanHistoryService.create(rQ.getIsbn(), Long.toString(rQ.getMemberId()), "",
            Common.getStringNowPlusDays(daysToLoan), "0.0", "issued").isSuccess) {
          reserveQueueRepository.deleteById(id);
          return new ActionConclusion(true, "Added to Loan successfully.");
        }
      }
      return new ActionConclusion(false, "Failed to add to Loan. Artifact not in stock.");
    }
    return new ActionConclusion(false, "Failed to add to Loan. Reserve ID does not exist.");
  }

  private void printMe(List<ReserveQueue> arr) {
    System.out.println("\n\nPrinting search result:");
    for (ReserveQueue reserveQueue : arr) {
      System.out.println(reserveQueue);
    }
    ;
  }
}