package ie.ucd.lms.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.LoanHistory;
import java.time.LocalDateTime;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
	List<LoanHistory> findByIssuedOnBetweenAndStatus(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo, String status,
			Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenAndStatusNot(LocalDateTime issuedOnFrom, LocalDateTime issuedOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByReturnOnBetweenAndStatus(LocalDateTime returnOnFrom, LocalDateTime returnOnTo, String status,
			Pageable pageable);

	List<LoanHistory> findByReturnOnBetweenAndStatusNot(LocalDateTime returnOnFrom, LocalDateTime returnOnTo,
			String status, Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenOrReturnOnBetweenAndStatus(LocalDateTime issuedOnFrom,
			LocalDateTime issuedOnTo, LocalDateTime returnOnFrom, LocalDateTime returnOnTo, String status, Pageable pageable);

	List<LoanHistory> findByIssuedOnBetweenOrReturnOnBetweenAndStatusNot(LocalDateTime issuedOnFrom,
			LocalDateTime issuedOnTo, LocalDateTime returnOnFrom, LocalDateTime returnOnTo, String status, Pageable pageable);

	boolean existsByIsbnAndMemberId(String isbn, Long memberId);
}