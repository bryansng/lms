package ie.ucd.lms.service;

import ie.ucd.lms.configuration.SecurityConfig;
import ie.ucd.lms.dao.LoginRepository;
import ie.ucd.lms.dao.MemberRepository;
import ie.ucd.lms.entity.Login;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import ie.ucd.lms.entity.Member;

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

    @Autowired
    MemberDetailsService memberDetailsService;

    public ActionConclusion login(String email, String password, HttpServletRequest request) {
        Member member = memberRepository.findByEmail(email);
        Login login = loginRepository.findByEmail(email);
        if (member != null && login != null) {
            if (login.getEmail().equals(email)
                    && securityConfig.getPasswordEncoder().matches(password, login.getHash())) {
                securityConfig.configAuth(login, securityConfig.getAuth(), member.getRoles());
                authenticateUserAndSetSession(member, request);
                return new ActionConclusion(true, "Logged in successfully.");
            }
            return new ActionConclusion(false, "Failed to login. Email or password incorrect.");
        }
        return new ActionConclusion(false, "Failed to login. Member does not exist.");
        // return securityConfig.getPasswordEncoder().matches(password, aLogin.getHash());
    }

    public ActionConclusion register(String fullName, String email, String password, String confirmPassword,
            HttpServletRequest request) {
        if (!password.equals(confirmPassword)) {
            return new ActionConclusion(false, "Failed to register. Passwords do not match.");
        }

        ActionConclusion acMemberCreate = memberService.create(email, password, fullName, "", "", "", "", "", "member",
                "USER");
        if (acMemberCreate.isSuccess) {
            Member member = memberRepository.findByEmail(email);
            Login login = loginRepository.findByEmail(email);
            securityConfig.configAuth(login, securityConfig.getAuth(), "USER");
            authenticateUserAndSetSession(member, request);
            return new ActionConclusion(true, "Registered successfully.");
        }
        return acMemberCreate;
    }

    public void authenticateUserAndSetSession(Member member, HttpServletRequest request) {
        UserDetails user = memberDetailsService.toUserDetails(member);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public void addMemberToModel(Model model, Authentication authentication) {
        if (isAuthenticated(authentication)) {
            Member member = getMemberFromUserObject(authentication);
            setMemberActiveOn(member);
            // System.out.println("is authenticated");
            model.addAttribute("member", member);
            model.addAttribute("memberInitials", member.getInitials());
        } else {
            // System.out.println("not authenticated");
        }
    }

    public Boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
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

    private void setMemberActiveOn(Member member) {
        member.setLastActiveOn(LocalDateTime.now());
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
}