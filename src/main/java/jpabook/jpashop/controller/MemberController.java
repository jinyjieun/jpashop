package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        //public String create(@Valid MemberForm form) -> 이렇게 코드를 짜면 오류가 발생할 경우 컨트롤러로 넘어오지 않고 튕겨버림.
        //public String create(@Valid MemberForm form, BindingResult result) -> 이렇게 짤경우 오류가 담겨서 컨트롤러 코드가 실행됨.

        //오류가 있을때 createMemberForm 으로 Return 한다.
        //Spring 과 thymeleaf 가 상당히 Integration 이 잘되어있다. Spring 이 Binding Result 를 화면까지 끌고가서 어떤 에러가 있는지 확인할 수 있음.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
}
