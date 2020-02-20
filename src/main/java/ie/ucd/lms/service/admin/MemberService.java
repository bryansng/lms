package ie.ucd.lms.service.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Member;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	public List<Member> search(String stringToSearch, int pageNum) {
		Long id = Common.convertStringToLong(stringToSearch);

		List<Member> res = memberRepository
				.findByIdOrFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrMobileNumberContainsOrAddressContainsIgnoreCaseOrTypeIgnoreCaseContains(
						id, stringToSearch, stringToSearch, stringToSearch, stringToSearch, stringToSearch,
						PageRequest.of(pageNum, Common.PAGINATION_ROWS));
		return res;
	}

	public Boolean update() {
		return false;
	}

	public Boolean isMember(String stringToSearch) {
		return !isLibrarian(stringToSearch);
	}

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

	private void printMe(List<Member> arr) {
		System.out.println("\n\nPrinting search result:");
		for (Member member : arr) {
			System.out.println(member);
		}
		;
	}

	private void printAll() {
		System.out.println("\n\nPrinting all:");
		for (Member member : memberRepository.findAll()) {
			System.out.println(member);
		}
		;
	}
}