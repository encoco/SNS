package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	@GetMapping(value =  {"","/", "/Signup","/index", "/BoarddWrite", "/mypage", "/UserPage/**", "/Message", "/Participate", "/Setting", "/Together"})
    public String forward() {
        return "forward:/index.html";
    }
}