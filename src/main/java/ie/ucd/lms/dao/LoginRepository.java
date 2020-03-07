package ie.ucd.lms.dao;

import ie.ucd.lms.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    Login findByEmail(String email);

    @Query("SELECT email FROM Login LOGIN WHERE LOGIN.email = ?1")
    String findEmailByEmail(String email);

    boolean existsByEmail(String email);
}