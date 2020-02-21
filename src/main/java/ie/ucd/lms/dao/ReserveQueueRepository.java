package ie.ucd.lms.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.ReserveQueue;
import java.time.LocalDateTime;

@Repository
public interface ReserveQueueRepository extends JpaRepository<ReserveQueue, Long> {
	List<ReserveQueue> findByExpiredOnBetween(LocalDateTime expiredOnFrom, LocalDateTime expiredOnTo, Pageable pageable);

	boolean existsByIsbnAndMemberId(String isbn, Long memberId);
}