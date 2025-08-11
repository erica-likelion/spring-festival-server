package likelion.festival.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.user.domain.User;
import likelion.festival.admin.dto.AdminLoginDto;
import likelion.festival.auth.dto.AuthCodeRequestDto;
import likelion.festival.auth.service.AuthService;
import likelion.festival.user.service.UserService;
import likelion.festival.auth.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/kakao/login")
    public ResponseEntity<String> getAuthCode(@RequestBody AuthCodeRequestDto authCodeResponseDto, HttpServletResponse response){
        authService.doLoginProcess(authCodeResponseDto.getCode(), response);
        return ResponseEntity.ok().body("Login successful");
    }

    @PostMapping("/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenUtils.getRefreshToken(request);
        Long userId = Long.valueOf(jwtTokenUtils.getClaims(refreshToken).getSubject());

        User user = userService.getUserById(userId);
        String token = jwtTokenUtils.generateAccessToken(user);
        response.setHeader("Authorization", "Bearer " + token);
        return "Token refreshed";
    }

    @PostMapping("/admin-login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminLoginDto dto, HttpServletResponse response) {
        authService.adminLogin(dto.getUsername(), dto.getPassword(), response);
        return ResponseEntity.ok("관리자 로그인이 완료되었습니다.");
    }
}
