package ie.ucd.lms.service;

import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public void save(Member member, Login login) {
        login.setMember(member);
        member.setLogin(login);

        memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Boolean isMember(String stringToSearch) {
        return !isLibrarian(stringToSearch);
    }

    // public Page<Member> search(String stringToSearch, int pageNum) {
    //     Long id = Common.convertStringToLong(stringToSearch);

    //     Page<Member> res = memberRepository
    //             .findByIdOrFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrMobileNumberContainsOrAddressContainsIgnoreCaseOrTypeIgnoreCaseContains(
    //                     id, stringToSearch, stringToSearch, stringToSearch, stringToSearch, stringToSearch,
    //                     PageRequest.of(pageNum, Common.PAGINATION_ROWS));
    //     return res;
    // }

    public Boolean isLibrarian(String stringToSearch) {
        Long id = Common.convertStringToLong(stringToSearch);

        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            if (member.get().getType().toLowerCase().equals("librarian")) {
                return true;
            }
        }
        return false;
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member createMember(Login login) {
        Member member = new Member();
        member.setEmail(login.getEmail());

        return member;
    }

    public Member findbyEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}