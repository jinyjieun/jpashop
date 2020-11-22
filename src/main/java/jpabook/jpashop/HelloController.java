package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") //해당 Url로 Method Mapping한다는 의미
    public String hello(Model model) {
        model.addAttribute("data", "hello!!!");
        return "hello"; //viewName을 Return해줌.
    }
}
