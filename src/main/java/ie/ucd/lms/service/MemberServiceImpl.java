package ie.ucd.lms.service;

import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public void save(Member member, Login login) {
        login.setMember(member);
        member.setLogin(login);
        
        memberRepository.save(member);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Member createMember(String email) {
        Member member = new Member();
        member.setEmail(email);

        return member;
    }
}