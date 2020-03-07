package ie.ucd.lms.service;

import ie.ucd.lms.configuration.SecurityConfig;
import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import ie.ucd.lms.entity.Member;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class LoginService {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    public boolean exists(Login login) {
        String email = login.getEmail();
        String password = login.getHash();

        Login aLogin = loginRepository.findByEmail(email);

        if (aLogin == null) {
            return false;
        }

        return securityConfig.getPasswordEncoder().matches(password, aLogin.getHash());
    }

    // public ActionConclusion create(String email, String password) {
    //     if (!loginRepository.existsByEmail(email)) {
    //         if (memberRepository.existsByEmail(email)) {
    //             Login login = new Login();
    //             Member member = memberRepository.findByEmail(email);
    //             login.setAll(email, securityConfig.getPasswordEncoder().encode(password), member);
    //             loginRepository.save(login);
    //             return new ActionConclusion(true, "Created successfully.");
    //         }
    //         return new ActionConclusion(false, "Failed to create Login, Member email does not exist.");
    //     }
    //     return new ActionConclusion(false, "Failed to create. Login email already exists.");
    // }

    public ActionConclusion authenticate(String email, String password, boolean isLogin) {
        if (isLogin && !loginRepository.existsByEmail(email)) {
            return new ActionConclusion(true, "Email does not exist.");
        } else if (!isLogin && loginRepository.existsByEmail(email)) {
            return new ActionConclusion(false, "Email already exists.");
        } else {
            String msg = isLogin ? "Successfully Logged In." : "Successfully Signed Up.";
            return new ActionConclusion(true, msg);
        }
    }

    public void addMemberToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = getMemberFromUserObject(authentication);
            System.out.println("is authenticated");
            model.addAttribute("member", member);
            model.addAttribute("memberInitials", member.getInitials());
        } else {
            System.out.println("not authenticated");
        }
    }

    /**
     * @param authentication
     * @return member if authentication is true, null if not.
     */
    public Member getMemberFromUserObject(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return memberRepository.findByEmail(user.getUsername());
        }
        return null;
    }
}