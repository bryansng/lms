package ie.ucd.lms.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Artifact;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
	Page<Artifact> findByIdAndTypeContainsIgnoreCaseOrTitleContainsIgnoreCaseAndTypeContainsIgnoreCaseOrIsbnContainsIgnoreCaseAndTypeContainsIgnoreCaseOrAuthorsContainsIgnoreCaseAndTypeContainsIgnoreCase(
			Long id, String type1, String title, String type2, String isbn, String type3, String authors, String type4,
			Pageable pageable);

	boolean existsByIsbn(String isbn);

	Artifact findByIsbn(String isbn);

	Integer countByCreatedOnAfter(LocalDateTime createdOn);

	List<Artifact> findTop6ByOrderByIdDescCreatedOnDesc();

	List<Artifact> findTop6ByOrderByTotalLoansDesc();
}
