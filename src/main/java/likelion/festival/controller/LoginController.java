package likelion.festival.controller;

import likelion.festival.dto.AuthCodeRequestDto;
import likelion.festival.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

//@RestController
//@RequiredArgsConstructor
//public class LoginController {
//    private final UserService userService;
//
//    /* 프론트에서 로그인 하고 받은 응답 코드 받는 메서드*/
//    @GetMapping("/login/kakao/auth-code")
//    public ResponseEntity<String> getAuthCode(@ModelAttribute AuthCodeRequestDto authCodeResponseDto){
//
//        if (authCodeResponseDto.getError() != null) {
//            URI redirectUri = URI.create("https://{frontend.com}/login?error=" + URLEncoder.encode(authCodeResponseDto.getError_description(), StandardCharsets.UTF_8));
//            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
//        }
//
//
//
//    }
//}
