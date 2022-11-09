package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.*;
import hwangjihun.coronamonitor.domain.constvalue.members.SessionConst;
import hwangjihun.coronamonitor.domain.file.FileStore;
import hwangjihun.coronamonitor.domain.file.UploadFile;
import hwangjihun.coronamonitor.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final FileStore fileStore;

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

    /**
     * Session의 id를 가지고 찾은 Member를 Model에 담아서 출력해준다.
     * userId, userName, age, profileImage를 변경할 수 있다.
     * @param request
     * @param model
     * @return profile View로 내이동한다.
     */
    @GetMapping("/profile")
    public String profileForm(HttpServletRequest request, Model model) {

        //TODO 데이터를 담아서 profile 화면에 보여주어야 한다.
        HttpSession session = request.getSession(false);

        if (session != null) {
            Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            Optional<Member> optionalMember = memberService.findById(sessionMember.getId());

            if (optionalMember.isEmpty()) {
                model.addAttribute("member", new Member());
            } else {
                Member findMember = optionalMember.get();
                model.addAttribute("member", findMember);
            }
        }

        return "/members/profile";
    }

    @PostMapping("/profile")
    public String profileEdit(@ModelAttribute uploadProfileDto uploadProfileDto, HttpServletRequest request) throws IOException {

        //TODO profileDto와 MemberUpdateDto의 의의가 겹쳤다. 의미 분리가 필요하지 않다면, 둘 중 하나 수정 혹은 제거가 필요하다.
        //TODO update 이전의 profileImage 제거.
        //TODO session에는 id를 넣고, 그 id를 가지고 조회하여 Member를 만들고, 그 Member를 model에 넣든 하여 보여주는게 맞는 것 같다.
        HttpSession session = request.getSession(false);
        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Optional<Member> optionalMember = memberService.findById(sessionMember.getId());
        Member findMember = optionalMember.isEmpty() ? null : optionalMember.get();

        if (findMember != null) {
            fileStore.deleteFile(findMember.getProfileImage());
            UploadFile profileImage = fileStore.storeFile(uploadProfileDto.getProfileImage());
            MemberUpdateDto memberUpdateDto = new MemberUpdateDto(
                    sessionMember.getPassword(),
                    uploadProfileDto.getUserName(),
                    uploadProfileDto.getAge(),
                    profileImage.getStoreFileName()
            );
            memberService.update(sessionMember.getId(), memberUpdateDto);
        }

        return "redirect:/members/profile";
    }
}
