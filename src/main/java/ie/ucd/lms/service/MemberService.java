package ie.ucd.lms.service;

import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;

public interface MemberService {
    void save(Member member, Login login);

    Member findByEmail(String email);

    Member createMember(Login login);
}