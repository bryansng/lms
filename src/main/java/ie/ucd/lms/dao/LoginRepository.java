package ie.ucd.lms.dao;

import ie.ucd.lms.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    String getPasswordByEmail(String email);

    String getEmailByEmail(String email);
}