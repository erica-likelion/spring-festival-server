package likelion.festival.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.domain.User;
import likelion.festival.dto.AdminLoginDto;
import likelion.festival.dto.AuthCodeRequestDto;
import likelion.festival.service.AuthService;
import likelion.festival.service.UserService;
import likelion.festival.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/auth/login/kakao/auth-code")
    public ResponseEntity<String> getAuthCode(@ModelAttribute AuthCodeRequestDto authCodeResponseDto, HttpServletResponse response){

        if (authCodeResponseDto.getError() != null) {
            URI redirectUri = URI.create("https://spring-festival-testing.vercel.app/login?error=" + URLEncoder.encode(authCodeResponseDto.getError(), StandardCharsets.UTF_8));
            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
        }

        authService.doLoginProcess(authCodeResponseDto.getCode(), response);
        return ResponseEntity.ok().body("Login successful");
    }

    @PostMapping("/auth/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenUtils.getRefreshToken(request);
        Long userId = Long.valueOf(jwtTokenUtils.getClaims(refreshToken).getSubject());

        User user = userService.getUserById(userId);
        String token = jwtTokenUtils.generateAccessToken(user);
        response.setHeader("Authorization", "Bearer " + token);
        return "Token refreshed";
    }

    @PostMapping("/auth/admin-login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminLoginDto dto, HttpServletResponse response) {
        authService.adminLogin(dto.getUsername(), dto.getPassword(), response);
        return ResponseEntity.ok("관리자 로그인이 완료되었습니다.");
    }
}
