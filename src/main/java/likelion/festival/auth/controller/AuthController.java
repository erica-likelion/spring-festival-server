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

/**
 * 인증과 관련된 API 요청을 처리하는 컨트롤러입니다.
 *
 * @author 김승민, 송재현
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * 카카오 인증 코드를 통해 카카오 회원가입 및 로그인을 처리합니다.
     * method: POST
     * url: /kakao/login
     *
     * @param authCodeResponseDto 카카오 인증 코드가 담긴 DTO
     * @param response HTTP 응답 객체 (토큰을 헤더에 추가하기 위해 사용)
     * @return 로그인 성공 메시지를 담은 {@link ResponseEntity}
     */
    @PostMapping("/kakao/login")
    public ResponseEntity<String> getAuthCode(@RequestBody AuthCodeRequestDto authCodeResponseDto, HttpServletResponse response){
        authService.doLoginProcess(authCodeResponseDto.getCode(), response);
        return ResponseEntity.ok().body("Login successful");
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.
     * method: POST
     * url: /refresh
     *
     * @param request HTTP 요청 객체 (리프레시 토큰을 가져오기 위해 사용)
     * @param response HTTP 응답 객체 (새 액세스 토큰을 헤더에 추가하기 위해 사용)
     * @return 토큰 갱신 성공 메시지
     */
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
