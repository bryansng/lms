package ie.ucd.lms.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Artifact;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
	List<Artifact> findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCase(Long id,
			String title, String isbn, String authors, Pageable pageable);

	List<Artifact> findByIdOrTitleContainsIgnoreCaseOrIsbnContainsIgnoreCaseOrAuthorsContainsIgnoreCaseAndType(Long id,
			String title, String isbn, String authors, String type, Pageable pageable);

	boolean existsByIsbn(String isbn);
}