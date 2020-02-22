package ie.ucd.lms.service.admin;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.ReserveQueueRepository;
import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.dao.LoanHistoryRepository;
import ie.ucd.lms.entity.ReserveQueue;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.Member;

@Service
public class ReportService {
	@Autowired
	ReserveQueueRepository reserveQueueRepository;

	@Autowired
	ArtifactRepository artifactRepository;

	@Autowired
	LoanHistoryRepository loanHistoryRepository;

	public LocalDateTime today = LocalDate.now().atStartOfDay();
	public LocalDateTime thisMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
	public LocalDateTime thisYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();

	public BigDecimal fine(LocalDateTime fromDate) {
		return loanHistoryRepository.sumFineByIssuedOnAfter(fromDate);
	}

	public Integer totalArtifacts(LocalDateTime fromDate) {
		return artifactRepository.countByCreatedOnAfter(fromDate);
	}

	public Integer artifactsIssued(LocalDateTime fromDate) {
		return loanHistoryRepository.countByIssuedOnAfter(fromDate);
	}

	public Integer artifactsReturned(LocalDateTime fromDate) {
		return loanHistoryRepository.countByReturnedOnAfter(fromDate);
	}

	public Integer artifactsLost(LocalDateTime fromDate) {
		return loanHistoryRepository.countByIssuedOnAfterAndWasLostTrue(fromDate);
	}
}