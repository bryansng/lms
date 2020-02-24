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
		printMe(res.getContent());
		return res;
	}

	public Page<LoanHistory> searchAllButLost(String artifact, String member, String fromDate, String toDate,
			String status, int pageNum) {
		Long artifactId = Common.convertStringToLong(artifact);
		Long memberId = Common.convertStringToLong(member);
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		Page<LoanHistory> res = loanHistoryRepository.findAllByArtifactAndMemberAndBothDatesAndStatusNot(artifactId,
				artifact, memberId, member, fromDateTime, toDateTime, "lost", pRequest);
		printMe(res.getContent());
		return res;
	}

	public Page<LoanHistory> searchAllButLost(String artifact, String member, String fromDate, String toDate,
			String status, String dateType, int pageNum) {
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
		}
		return new PageImpl<LoanHistory>(new ArrayList<LoanHistory>()); // return empty list.
	}

	public Page<LoanHistory> searchLost(String artifact, String member, String fromDate, String toDate, String status,
			int pageNum) {
		Long artifactId = Common.convertStringToLong(artifact);
		Long memberId = Common.convertStringToLong(member);
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		Page<LoanHistory> res = loanHistoryRepository.findAllByArtifactAndMemberAndIssuedDateAndStatus(artifactId, artifact,
				memberId, member, fromDateTime, toDateTime, "lost", pRequest);
		return res;
	}

	public Boolean update(String stringId, String isbn, String memberId, String returnOn, String fine, String status) {
		Long id = Common.convertStringToLong(stringId);
		Long aMemberId = Common.convertStringToLong(memberId);

		if (loanHistoryRepository.existsById(id)) {
			if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
				Artifact artifact = artifactRepository.findByIsbn(isbn);
				Member member = memberRepository.getOne(aMemberId);
				LoanHistory loanHistory = loanHistoryRepository.getOne(id);
				loanHistory.setAll(isbn, memberId, returnOn, fine, status, artifact, member);
				loanHistoryRepository.save(loanHistory);
				return true;
			}
		}
		return false;
	}

	public Boolean create(String isbn, String memberId, String returnOn, String fine, String status) {
		Long aMemberId = Common.convertStringToLong(memberId);

		if (!loanHistoryRepository.existsByIsbnAndMemberId(isbn, aMemberId)) {
			if (artifactRepository.existsByIsbn(isbn) && memberRepository.existsById(aMemberId)) {
				Artifact artifact = artifactRepository.findByIsbn(isbn);
				Member member = memberRepository.getOne(aMemberId);
				if (artifact.inStock()) {
					LoanHistory loanHistory = new LoanHistory();
					loanHistory.setAll(isbn, memberId, returnOn, fine, status, artifact, member);
					loanHistoryRepository.save(loanHistory);
					return true;
				} else {
					return reserveQueueService.create(isbn, memberId, Common.DEFAULT_EXPIRED_ON);
				}
			}
		}
		return false;
	}

	public Boolean delete(String stringId) {
		Long id = Common.convertStringToLong(stringId);

		if (loanHistoryRepository.existsById(id)) {
			loanHistoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Boolean restocked(String stringId) {
		Long id = Common.convertStringToLong(stringId);

		if (loanHistoryRepository.existsById(id)) {
			LoanHistory loanHistory = loanHistoryRepository.getOne(id);
			loanHistory.setStatus("restocked");

			Artifact artifact = loanHistory.getArtifact();
			artifact.incrementQuantity();
			loanHistoryRepository.save(loanHistory);
			return true;
		}
		return false;
	}

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

	public Boolean renew(String stringId, String daysToRenew) {
		Long id = Common.convertStringToLong(stringId);
		Long days = Common.convertStringToLong(daysToRenew);

		if (loanHistoryRepository.existsById(id)) {
			LoanHistory loanHistory = loanHistoryRepository.getOne(id);
			if (reserveQueueRepository.existsByIsbn(loanHistory.getIsbn())) {
				returnn(stringId);
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