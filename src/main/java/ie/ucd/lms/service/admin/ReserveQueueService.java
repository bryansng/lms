package ie.ucd.lms.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.ReserveQueueRepository;
import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.ReserveQueue;
import ie.ucd.lms.entity.Artifact;
import ie.ucd.lms.entity.Member;

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

	public Page<ReserveQueue> search(String artifact, String member, String fromDate, String toDate, int pageNum) {
		Long artifactId = Common.convertStringToLong(artifact);
		Long memberId = Common.convertStringToLong(member);
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		Page<ReserveQueue> res = reserveQueueRepository.findAllByArtifactAndMemberAndBothDatesAndStatus(artifactId,
				artifact, memberId, member, fromDateTime, toDateTime, pRequest);
		// Page<ReserveQueue> res = reserveQueueRepository.findByExpiredOnBetween(fromDateTime, toDateTime, pRequest);
		return res;
	}

	public ReserveQueue nextInLine(String isbn) {
		return reserveQueueRepository.findFirstByIsbnOrderById(isbn);
	}

	public Boolean update(String stringId, String isbn, String memberId, String expiredOn) {
		Long id = Common.convertStringToLong(stringId);
		Long aMemberId = Common.convertStringToLong(memberId);

		if (reserveQueueRepository.existsById(id)) {
			if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
				Artifact artifact = artifactRepository.findByIsbn(isbn);
				Member member = memberRepository.getOne(aMemberId);
				ReserveQueue reserveQueue = reserveQueueRepository.getOne(id);
				reserveQueue.setAll(isbn, memberId, expiredOn, artifact, member);
				reserveQueueRepository.save(reserveQueue);
				return true;
			}
		}
		return false;
	}

	public Boolean create(String isbn, String memberId, String expiredOn) {
		Long aMemberId = Common.convertStringToLong(memberId);

		if (!reserveQueueRepository.existsByIsbnAndMemberId(isbn, aMemberId)) {
			if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
				Artifact artifact = artifactRepository.findByIsbn(isbn);
				Member member = memberRepository.getOne(aMemberId);
				ReserveQueue reserveQueue = new ReserveQueue();
				reserveQueue.setAll(isbn, memberId, expiredOn, artifact, member);
				reserveQueueRepository.save(reserveQueue);
				return true;
			}
		}
		return false;
	}

	public Boolean delete(String stringId) {
		Long id = Common.convertStringToLong(stringId);

		if (reserveQueueRepository.existsById(id)) {
			reserveQueueRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Boolean loan(String stringId, String daysToLoan) {
		Long id = Common.convertStringToLong(stringId);

		if (reserveQueueRepository.existsById(id)) {
			ReserveQueue rQ = reserveQueueRepository.getOne(id);
			loanHistoryService.create(rQ.getIsbn(), Long.toString(rQ.getMemberId()), Common.getStringNowPlusDays(daysToLoan),
					"0.0", "issued");
			reserveQueueRepository.deleteById(id);
			return true;
		}
		return false;
	}

	private void printMe(List<ReserveQueue> arr) {
		System.out.println("\n\nPrinting search result:");
		for (ReserveQueue reserveQueue : arr) {
			System.out.println(reserveQueue);
		}
		;
	}
}