package ie.ucd.lms.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	List<Member> findByIdOrFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrMobileNumberContainsOrAddressContainsIgnoreCaseOrTypeIgnoreCaseContains(
			Long id, String fullName, String email, String mobileNumber, String address, String type, Pageable pageable);
}