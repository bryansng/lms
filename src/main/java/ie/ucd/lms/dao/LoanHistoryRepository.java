package ie.ucd.lms.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.LoanHistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
	List<LoanHistory> findByIssuedOnBetweenAndStatusIgnoreCase(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenAndStatusNotIgnoreCase(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByReturnOnBetweenAndStatusIgnoreCase(LocalDateTime returnOnFrom, LocalDateTime returnOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByReturnOnBetweenAndStatusNotIgnoreCase(LocalDateTime returnOnFrom, LocalDateTime returnOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenAndStatusIgnoreCaseOrReturnOnBetweenAndStatusIgnoreCase(
			LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo, String status1, LocalDateTime returnOnFrom,
			LocalDateTime returnOnTo, String status2, Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenAndStatusNotIgnoreCaseOrReturnOnBetweenAndStatusNotIgnoreCase(
			LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo, String status1, LocalDateTime returnOnFrom,
			LocalDateTime returnOnTo, String status2, Pageable pageable);

	boolean existsByIsbnAndMemberId(String isbn, Long memberId);

	@Query("SELECT SUM(LH.fine) FROM LoanHistory LH WHERE LH.issuedOn > ?1")
	BigDecimal sumFineByIssuedOnAfter(LocalDateTime issuedOn);

	Integer countByIssuedOnAfter(LocalDateTime issuedOn);

	Integer countByReturnedOnAfter(LocalDateTime returnedOn);

	Integer countByIssuedOnAfterAndWasLostTrue(LocalDateTime issuedOn);
}