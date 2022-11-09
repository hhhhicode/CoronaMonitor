package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.LoginDto;
import hwangjihun.coronamonitor.domain.Member;
import hwangjihun.coronamonitor.domain.MemberRegisterDto;
import hwangjihun.coronamonitor.domain.ProfileDto;
import hwangjihun.coronamonitor.domain.constvalue.members.SessionConst;
import hwangjihun.coronamonitor.repository.MemberRepository;
import hwangjihun.coronamonitor.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * member 의 profile 사진을 업로드할 경로
     */
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("member") Member member) {

        return "/members/register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("member") MemberRegisterDto memberParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("{} errors = {}", "MemberController.register", bindingResult);
            return "/members/register";
        }

        // 성공 로직

        Member member = new Member();
        member.setUserId(memberParam.getUserId());
        member.setPassword(memberParam.getPassword());
        member.setAge(memberParam.getAge());
        member.setUserName(memberParam.getUserName());

        memberService.save(member);
        log.info("Register : {}", member);
        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginDto") LoginDto loginDto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            return "redirect:/";
        }
        return "/members/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request, Model model) {

        Member loginMember = memberService.login(loginDto.getLoginId(), loginDto.getPassword());
        log.info("Login : {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("memberLogin", null, "로그인 정보가 맞지 않습니다.");
            return "/members/login";
        }

        //로그인 성공시

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        log.info("로그인 성공 = {}", loginMember);
        model.addAttribute("loginMember", loginMember);

        return "redirect:/home";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/home";
    }

    @GetMapping("/profile")
    public String profileForm(HttpServletRequest request, Model model, @ModelAttribute("member") Member member) {

        //TODO 데이터를 담아서 profile 화면에 보여주어야 한다.
        HttpSession session = request.getSession(false);

        if (session != null) {
            Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            member.setId(sessionMember.getId());
            member.setUserId(sessionMember.getUserId());
            member.setPassword(sessionMember.getPassword());
            member.setUserName(sessionMember.getUserName());
            member.setAge(sessionMember.getAge());
        }

        return "/members/profile";
    }

    //TODO 프로필 사진, 비밀번호, 유저이름, 나이 변경
    @PostMapping("/profile")
    public String profileEdit(@ModelAttribute ProfileDto profileDto) {

        //TODO 데이터베이스에 저장

        return "redirect:/members/profile";
    }
}
