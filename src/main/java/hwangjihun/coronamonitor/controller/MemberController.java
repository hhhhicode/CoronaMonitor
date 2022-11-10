package hwangjihun.coronamonitor.controller;

import hwangjihun.coronamonitor.domain.constvalue.members.SessionConst;
import hwangjihun.coronamonitor.domain.file.FileStore;
import hwangjihun.coronamonitor.domain.file.UploadFile;
import hwangjihun.coronamonitor.domain.members.*;
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
        session.setAttribute(SessionConst.LOGIN_ID, loginMember.getId());

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
     * @param model
     * @return profile View로 내이동한다.
     */
    @GetMapping("/profile")
    public String profileForm(
            @SessionAttribute(name = SessionConst.LOGIN_ID, required = false) Long loginId,
            Model model) {

        if (loginId == null) {
            return "redirect:/members/login";
        }
        Optional<Member> optionalMember = memberService.findById(loginId);

        if (optionalMember.isEmpty()) {
            model.addAttribute("member", new Member());
        } else {
            model.addAttribute("member", optionalMember.get());
        }

        return "/members/profile";
    }

    @PostMapping("/profile")
    public String profileEdit(
            @ModelAttribute uploadProfileDto uploadProfileDto,
            @SessionAttribute(name = SessionConst.LOGIN_ID, required = false) Long loginId) throws IOException {

        //TODO profileDto와 MemberUpdateDto의 의의가 겹쳤다. 의미 분리가 필요하지 않다면, 둘 중 하나 수정 혹은 제거가 필요하다.
        if (loginId == null) return "redirect:/members/login";
        Optional<Member> optionalMember = memberService.findById(loginId);
        Member findMember = optionalMember.isEmpty() ? null : optionalMember.get();

        if (findMember != null) {
            //프로필 사진 제거 및 업로드
            String storeFileName = findMember.getProfileImage();
            if (!uploadProfileDto.getProfileImage().isEmpty()) {
                fileStore.deleteFile(findMember.getProfileImage());
                storeFileName = fileStore
                        .storeFile(uploadProfileDto.getProfileImage())
                        .getStoreFileName();
            }

            MemberUpdateDto memberUpdateDto = new MemberUpdateDto(
                    findMember.getPassword(),
                    uploadProfileDto.getUserName(),
                    uploadProfileDto.getAge(),
                    storeFileName
            );
            memberService.update(findMember.getId(), memberUpdateDto);
        }

        return "redirect:/members/profile";
    }
}
