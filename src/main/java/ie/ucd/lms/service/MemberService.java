package ie.ucd.lms.service;

import ie.ucd.lms.entity.Member;

public interface MemberService {
    void save(Member member);

    Member findByEmail(String email);
}