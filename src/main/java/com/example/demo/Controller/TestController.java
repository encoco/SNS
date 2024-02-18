package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class TestController {
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	@GetMapping("/LoginPage")
	public void login() {
		System.out.println("컨트롤러 들어왔다잉");
		return;
	}
	@PostMapping("/Signup")
	public void Signup(@RequestBody SignupRequest request) {
	    System.out.println(request.getId());
	    System.out.println(request.getPassword());
	    System.out.println(request.getPhone());
	    System.out.println(request.getEmail());
	}
	@GetMapping("/Signup")
	public void Signups(@RequestBody SignupRequest request) {
		System.out.println("hi");
	    System.out.println(request.getId());
	    System.out.println(request.getPassword());
	    System.out.println(request.getPhone());
	    System.out.println(request.getEmail());
	}

}

