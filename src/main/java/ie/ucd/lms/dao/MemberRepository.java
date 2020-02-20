package ie.ucd.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ie.ucd.lms.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}