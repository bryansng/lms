package ie.ucd.lms.dao;

import ie.ucd.lms.entity.Member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Page<Member> findByIdOrFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrMobileNumberContainsOrAddressContainsIgnoreCaseOrTypeIgnoreCaseContains(
      Long id, String fullName, String email, String mobileNumber, String address, String type, Pageable pageable);

  boolean existsByEmail(String email);

  Member findByEmail(String email);

  @Query("SELECT m FROM Member m WHERE m.email = ?1")
  Optional<Member> findByEmailOptional(String email);

}