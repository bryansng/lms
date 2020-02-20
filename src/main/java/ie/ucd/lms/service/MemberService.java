package ie.ucd.lms.service;

import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class MemberService implements MemberServiceInterface {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LoginRepository loginRepository;

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}
