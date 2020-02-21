package ie.ucd.lms.service.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.LoanHistoryRepository;
import ie.ucd.lms.entity.LoanHistory;

@Service
public class LoanHistoryService {
	@Autowired
	LoanHistoryRepository loanHistoryRepository;

	public List<LoanHistory> searchAllButLost(String fromDate, String toDate, int pageNum) {
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		List<LoanHistory> res = loanHistoryRepository
				.findByIssuedOnBetweenAndStatusNotIgnoreCaseOrReturnOnBetweenAndStatusNotIgnoreCase(fromDateTime, toDateTime,
						"lost", fromDateTime, toDateTime, "lost", pRequest);
		return res;
	}

	public List<LoanHistory> searchAllButLost(String fromDate, String toDate, String dateType, int pageNum) {
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		switch (dateType.toLowerCase()) {
			case "issued date":
				return loanHistoryRepository.findByIssuedOnBetweenAndStatusNotIgnoreCase(fromDateTime, toDateTime, "lost",
						pRequest);
			case "return date":
				return loanHistoryRepository.findByReturnOnBetweenAndStatusNotIgnoreCase(fromDateTime, toDateTime, "lost",
						pRequest);
			default:
		}
		return new ArrayList<LoanHistory>(); // return empty list.
	}

	public List<LoanHistory> searchLost(String fromDate, String toDate, int pageNum) {
		LocalDateTime fromDateTime = Common.getLowerBoundOfDate(fromDate);
		LocalDateTime toDateTime = Common.getUpperBoundOfDate(toDate);
		PageRequest pRequest = PageRequest.of(pageNum, Common.PAGINATION_ROWS);

		List<LoanHistory> res = loanHistoryRepository.findByIssuedOnBetweenAndStatusIgnoreCase(fromDateTime, toDateTime,
				"lost", pRequest);
		return res;
	}

	public Boolean update(String stringId, String isbn, String memberId, String returnOn, String fine, String status) {
		Long id = Common.convertStringToLong(stringId);

		if (loanHistoryRepository.existsById(id)) {
			LoanHistory loanHistory = loanHistoryRepository.getOne(id);
			loanHistory.setAll(isbn, memberId, returnOn, fine, status);
			loanHistoryRepository.save(loanHistory);
			return true;
		}
		return false;
	}

	public Boolean create(String isbn, String memberId, String returnOn, String fine, String status) {
		Long aMemberId = Common.convertStringToLong(memberId);

		if (!loanHistoryRepository.existsByIsbnAndMemberId(isbn, aMemberId)) {
			LoanHistory loanHistory = new LoanHistory();
			loanHistory.setAll(isbn, memberId, returnOn, fine, status);
			loanHistoryRepository.save(loanHistory);
			return true;
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
}