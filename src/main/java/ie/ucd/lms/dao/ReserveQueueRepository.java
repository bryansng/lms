package ie.ucd.lms.dao;

import ie.ucd.lms.entity.Member;
import ie.ucd.lms.entity.ReserveQueue;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveQueueRepository extends JpaRepository<ReserveQueue, Long> {
  @Query("SELECT RQ " + "FROM ReserveQueue RQ " + "WHERE "
      + "(RQ.artifact.id = ?1 or UPPER(RQ.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(RQ.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
      + "(RQ.memberId = ?3 or UPPER(RQ.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(RQ.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
      + "(RQ.expiredOn between ?5 and ?6)")
  Page<ReserveQueue> findAllByArtifactAndMemberAndBothDatesAndStatus(Long artifactId, String artifactDetails,
      Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

  Page<ReserveQueue> findByExpiredOnBetween(LocalDateTime expiredOnFrom, LocalDateTime expiredOnTo, Pageable pageable);

  ReserveQueue findFirstByIsbnOrderById(String isbn);

  boolean existsByIsbnAndMemberId(String isbn, Long memberId);

  boolean existsByIsbn(String isbn);

  boolean existsByArtifactId(String artifactId);

  List<ReserveQueue> findByMember(Member member);
}
