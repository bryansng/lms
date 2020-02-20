package ie.ucd.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}