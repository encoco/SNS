package com.example.demo.Config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		//registrationId 로 어디서 로그인했는지 ex)google,kakao, naver
//		System.out.println("userclient  : " + userRequest.getClientRegistration());
//		//구글로그인 버튼 클릭 -> 구글 로그이낭 -> 로그인 완료 -> code리턴(OAUth-client 라이브러리) -> AccessToken 요청 == userRequest정보
//		// -> 회원 프로필 받아야함 (loadUser 메서드) -> 구글로부터 회원 프로필을 받아줌.
//		System.out.println("useraccesstoken : " + userRequest.getAccessToken().getTokenValue());
		System.out.println("userattribute  : " + super.loadUser(userRequest).getAttributes());

		OAuth2User oauth2User = super.loadUser(userRequest);
		return super.loadUser(userRequest);
	}
}
