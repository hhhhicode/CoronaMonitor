package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.LoginDto;
import hwangjihun.coronamonitor.domain.Member;
import hwangjihun.coronamonitor.domain.MemberRegisterDto;
import hwangjihun.coronamonitor.domain.constvalue.members.SessionConst;
import hwangjihun.coronamonitor.repository.MemberRepository;
import hwangjihun.coronamonitor.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

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
}
