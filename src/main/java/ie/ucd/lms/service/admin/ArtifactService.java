package ie.ucd.lms.service.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.ArtifactRepository;
import ie.ucd.lms.entity.Artifact;

@Service
public class ArtifactService {
	@Autowired
	ArtifactRepository artifactRepository;

	public List<Artifact> search(String stringToSearch, int pageNum) {
		Long id = Common.convertStringToLong(stringToSearch);

		List<Artifact> res = artifactRepository
				.findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCase(id, stringToSearch,
						stringToSearch, stringToSearch, PageRequest.of(pageNum, Common.PAGINATION_ROWS));
		return res;
	}

	public List<Artifact> search(String stringToSearch, String type, int pageNum) {
		Long id = Common.convertStringToLong(stringToSearch);

		List<Artifact> res = artifactRepository
				.findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCaseAndType(id, stringToSearch,
						stringToSearch, stringToSearch, type, PageRequest.of(pageNum, Common.PAGINATION_ROWS));
		return res;
	}

	public Boolean update(String stringId, String isbn, String type, String genre, String authors, String title,
			String subtitle, String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String totalQuantity, String rackLocation) {
		Long id = Common.convertStringToLong(stringId);

		if (artifactRepository.existsById(id)) {
			Artifact artifact = artifactRepository.getOne(id);
			artifact.setAll(isbn, type, genre, authors, title, subtitle, description, publishers, publishedOn, itemPrice,
					quantity, totalQuantity, rackLocation);
			artifactRepository.save(artifact);
			return true;
		}
		return false;
	}

	public Boolean create(String isbn, String type, String genre, String authors, String title, String subtitle,
			String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String totalQuantity, String rackLocation) {
		if (!artifactRepository.existsByIsbn(isbn)) {
			Artifact artifact = new Artifact();
			artifact.setAll(isbn, type, genre, authors, title, subtitle, description, publishers, publishedOn, itemPrice,
					quantity, totalQuantity, rackLocation);
			artifactRepository.save(artifact);
			return true;
		}
		return false;
	}

	public Boolean delete(String stringId) {
		Long id = Common.convertStringToLong(stringId);

		if (artifactRepository.existsById(id)) {
			artifactRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public void printAll() {
		System.out.println("\n\nPrinting search result:");
		for (Artifact artifact : artifactRepository.findAll()) {
			System.out.println(artifact);
		}
		;
	}

	public void printAllWithLoanHistory() {
		System.out.println("\n\nPrinting search result:");
		for (Artifact artifact : artifactRepository.findAll()) {
			System.out.println(artifact.toStringWithLoanHistory());
		}
		;
	}

	public void printAllWithReserveQueue() {
		System.out.println("\n\nPrinting search result:");
		for (Artifact artifact : artifactRepository.findAll()) {
			System.out.println(artifact.toStringWithReserveQueue());
		}
		;
	}
}