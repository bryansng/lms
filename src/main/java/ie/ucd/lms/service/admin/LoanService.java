package ie.ucd.lms.service.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.LoanHistoryRepository;
import ie.ucd.lms.entity.Artifact;

@Service
public class LoanService {
	@Autowired
	LoanHistoryRepository loanHistoryRepository;

	public List<Artifact> search(String stringToSearch, int pageNum) {
		Long id = Common.convertStringToLong(stringToSearch);

		List<Artifact> res = loanHistoryRepository
				.findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCase(id, stringToSearch,
						stringToSearch, stringToSearch, PageRequest.of(pageNum, Common.PAGINATION_ROWS));
		return res;
	}

	public List<Artifact> search(String stringToSearch, String type, int pageNum) {
		Long id = Common.convertStringToLong(stringToSearch);

		List<Artifact> res = loanHistoryRepository
				.findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCaseAndType(id, stringToSearch,
						stringToSearch, stringToSearch, type, PageRequest.of(pageNum, Common.PAGINATION_ROWS));
		return res;
	}

	public Boolean update(String stringId, String isbn, String type, String genre, String authors, String title,
			String subtitle, String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String rackLocation) {
		Long id = Common.convertStringToLong(stringId);

		if (loanHistoryRepository.existsById(id)) {
			System.out.println("exists");
			Artifact artifact = loanHistoryRepository.getOne(id);
			artifact.setAll(isbn, type, genre, authors, title, subtitle, description, publishers, publishedOn, itemPrice,
					quantity, rackLocation);
			loanHistoryRepository.save(artifact);
			return true;
		}
		return false;
	}

	public Boolean create(String isbn, String type, String genre, String authors, String title, String subtitle,
			String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String rackLocation) {
		if (!loanHistoryRepository.existsByIsbn(isbn)) {
			System.out.println("does not exists");
			Artifact artifact = new Artifact();
			artifact.setAll(isbn, type, genre, authors, title, subtitle, description, publishers, publishedOn, itemPrice,
					quantity, rackLocation);
			loanHistoryRepository.save(artifact);
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
}