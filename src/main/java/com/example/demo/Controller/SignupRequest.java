package com.example.demo.Controller;

import lombok.Data;

@Data
public class SignupRequest {
	   	private String id;
	    private String password;
	    private int phone; // int에서 String으로 변경하는 것을 고려, 전화번호 형식에 맞게
	    private String email;
}
