package ie.ucd.lms.dao;

import ie.ucd.lms.entity.LoanHistory;
import ie.ucd.lms.entity.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
	// @Query("SELECT LH FROM LoanHistory LH WHERE LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))")
	// Page<LoanHistory> matchv2(Long artifactId, String artifactDetails, Pageable pageable);
	/**
	 * Only one date
	 * (ArtifactId Or ArtifactTitle Or Isbn) And (MemberId Or Member_FullName Or Member_Email) And IssuedOnBetween And StatusIgnoreCase
	 * (P v Q v R) ^ (S v T v U) ^ V ^ A
	 * (P || Q || R) && (S || T || U) && V && A
	 * Artifact And Member And ReturnOn And Status
	 *
	 * Both dates
	 * (ArtifactId Or ArtifactTitle Or Isbn) And (MemberId Or Member_FullName Or Member_Email) And (IssuedOnBetween Or ReturnOnBetween) And StatusIgnoreCase
	 * (P v Q v R) ^ (S v T v U) ^ (V v A) ^ B
	 * (P || Q || R) && (S || T || U) && (V || A) && B
	 */
	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "((LH.issuedOn between ?5 and ?6) or (LH.returnOn between ?5 and ?6)) and "
			+ "UPPER(LH.status) like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndBothDatesAndStatus(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "((LH.issuedOn between ?5 and ?6) or (LH.returnOn between ?5 and ?6)) and "
			+ "UPPER(LH.status) not like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndBothDatesAndStatusNot(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "(LH.issuedOn between ?5 and ?6) and " + "UPPER(LH.status) like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndIssuedDateAndStatus(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "(LH.issuedOn between ?5 and ?6) and " + "UPPER(LH.status) not like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndIssuedDateAndStatusNot(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "(LH.returnOn between ?5 and ?6) and " + "UPPER(LH.status) like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndReturnDateAndStatus(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	@Query("SELECT LH " + "FROM LoanHistory LH " + "WHERE "
			+ "(LH.artifact.id = ?1 or UPPER(LH.artifact.title) like UPPER(CONCAT('%', UPPER(?2), '%')) or UPPER(LH.isbn) like UPPER(CONCAT('%', UPPER(?2), '%'))) and "
			+ "(LH.memberId = ?3 or UPPER(LH.member.fullName) like UPPER(CONCAT('%', UPPER(?4), '%')) or UPPER(LH.member.email) like UPPER(CONCAT('%', UPPER(?4), '%'))) and "
			+ "(LH.returnOn between ?5 and ?6) and " + "UPPER(LH.status) not like UPPER(CONCAT('%', UPPER(?7), '%'))")
	Page<LoanHistory> findAllByArtifactAndMemberAndReturnDateAndStatusNot(Long artifactId, String artifactDetails,
			Long memberId, String memberDetails, LocalDateTime dateFrom, LocalDateTime dateTo, String status,
			Pageable pageable);

	boolean existsByIsbnAndMemberId(String isbn, Long memberId);

	@Query("SELECT SUM(LH.fine) FROM LoanHistory LH WHERE LH.issuedOn > ?1")
	BigDecimal sumFineByIssuedOnAfter(LocalDateTime issuedOn);

	Integer countByIssuedOnAfter(LocalDateTime issuedOn);

	Integer countByReturnedOnAfter(LocalDateTime returnedOn);

	Integer countByIssuedOnAfterAndWasLostTrue(LocalDateTime issuedOn);

	List<LoanHistory> findByMember(Member member);

	/* 	Page<LoanHistory> findByArtifact_IdOrArtifact_TitleContainsIgnoreCaseOrIsbnContainsOrMember_FullNameContainsIgnoreCaseOrMember_EmailContainsIgnoreCaseOrMemberId(
				Long ArtifactId, String ArtifactTitle, String isbn, String memberFullName, String email, Long memberId,
				Pageable pageable);
	
		Page<LoanHistory> findByIssuedOnBetweenAndStatusIgnoreCase(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo,
				String status, Pageable pageable);
	
		Page<LoanHistory> findByIssuedOnBetweenAndStatusNotIgnoreCase(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo,
				String status, Pageable pageable);
	
		Page<LoanHistory> findByReturnOnBetweenAndStatusIgnoreCase(LocalDateTime returnOnFrom, LocalDateTime returnOnTo,
				String status, Pageable pageable);
	
		Page<LoanHistory> findByReturnOnBetweenAndStatusNotIgnoreCase(LocalDateTime returnOnFrom, LocalDateTime returnOnTo,
				String status, Pageable pageable);
	
		Page<LoanHistory> findByIssuedOnBetweenAndStatusIgnoreCaseOrReturnOnBetweenAndStatusIgnoreCase(
				LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo, String status1, LocalDateTime returnOnFrom,
				LocalDateTime returnOnTo, String status2, Pageable pageable);
	
		Page<LoanHistory> findByIssuedOnBetweenAndStatusNotIgnoreCaseOrReturnOnBetweenAndStatusNotIgnoreCase(
				LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo, String status1, LocalDateTime returnOnFrom,
				LocalDateTime returnOnTo, String status2, Pageable pageable); */
}
