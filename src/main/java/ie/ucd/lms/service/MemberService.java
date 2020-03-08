package ie.ucd.lms.service;

import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Login;
import ie.ucd.lms.entity.Member;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.configuration.SecurityConfig;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	SecurityConfig securityConfig;

	public void save(Member member, Login login) {
		login.setMember(member);
		member.setLogin(login);

		memberRepository.save(member);
	}

	public Page<Member> search(String stringToSearch, int pageNum) {
		return this.search(stringToSearch, pageNum, Common.PAGINATION_ROWS);
	}

	public Page<Member> search(String stringToSearch, int pageNum, int pageSize) {
		Long id = Common.convertStringToLong(stringToSearch);

		Page<Member> res = memberRepository
				.findByIdOrFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrMobileNumberContainsOrAddressContainsIgnoreCaseOrTypeIgnoreCaseContains(
						id, stringToSearch, stringToSearch, stringToSearch, stringToSearch, stringToSearch,
						PageRequest.of(pageNum, pageSize));
		return res;
	}

	public ActionConclusion update(String stringId, String email, String fullName, String mobileNumber, String address,
			String website, String bornOn, String bio, String type) {
		Long id = Common.convertStringToLong(stringId);

		if (memberRepository.existsById(id)) {
			Member member = memberRepository.getOne(id);
			member.setAll(email, fullName, mobileNumber, address, website, bornOn, bio, type);
			memberRepository.save(member);
			return new ActionConclusion(true, "Updated successfully.");
		}
		return new ActionConclusion(false, "Failed to update. Member ID does not exist.");
	}

	public ActionConclusion create(String email, String password, String fullName, String mobileNumber, String address,
			String website, String bornOn, String bio, String type, String roles) {
		if (!memberRepository.existsByEmail(email)) {
			Member member = new Member();
			Login login = new Login();
			login.setAll(email, securityConfig.getPasswordEncoder().encode(password));
			member.setAll(email, fullName, mobileNumber, address, website, bornOn, bio, type, login);
			memberRepository.save(member);
			return new ActionConclusion(true, "Created successfully.");
		}
		return new ActionConclusion(false, "Failed to create. Member email already exists.");
	}

	public Member createMember(Login login) {
		Member member = new Member();
		member.setEmail(login.getEmail());
		member.setRoles("USER"); // set as ROLE_USER by default.
		return member;
	}

	public ActionConclusion delete(String stringId) {
		Long id = Common.convertStringToLong(stringId);

		if (memberRepository.existsById(id)) {
			memberRepository.deleteById(id);
			return new ActionConclusion(true, "Deleted successfully.");
		}
		return new ActionConclusion(false, "Failed to delete. Member ID does not exist.");
	}

	public void printMe(List<Member> arr) {
		System.out.println("\n\nPrinting search result:");
		for (Member member : arr) {
			System.out.println(member);
		}
		;
	}

	public void printAll() {
		System.out.println("\n\nPrinting all:");
		for (Member member : memberRepository.findAll()) {
			System.out.println(member);
		}
		;
	}

	public void printAllWithLoanHistory() {
		System.out.println("\n\nPrinting search result:");
		for (Member member : memberRepository.findAll()) {
			System.out.println(member.toStringWithLoanHistory());
		}
		;
	}

	public void printAllWithReserveQueue() {
		System.out.println("\n\nPrinting search result:");
		for (Member member : memberRepository.findAll()) {
			System.out.println(member.toStringWithReserveQueue());
		}
		;
	}
}