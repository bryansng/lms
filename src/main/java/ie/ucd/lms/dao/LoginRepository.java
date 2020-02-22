package ie.ucd.lms.dao;

import ie.ucd.lms.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    // Login findByHash(String email);

    // @Query("SELECT email FROM Login WHERE LOWER(Login.email) = LOWER(:email)")
    // String getEmailByEmail(@Param("email") String email);
    Login findByEmail(String email);
}