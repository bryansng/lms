package ie.ucd.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Artifact;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
}