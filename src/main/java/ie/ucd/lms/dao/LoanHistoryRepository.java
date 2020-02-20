package ie.ucd.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.LoanHistory;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
}