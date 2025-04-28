package likelion.festival.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.domain.User;
import likelion.festival.dto.KakaoUserInfo;
import likelion.festival.repository.UserRepository;
import likelion.festival.utils.JwtTokenUtils;
import likelion.festival.utils.KakaoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final KakaoUtils kakaoUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final Integer maxAge;

    public AuthService(
            KakaoUtils kakaoUtils,
            UserRepository userRepository,
            UserService userService,
            JwtTokenUtils jwtTokenUtils,
            @Value("${jwt.refresh-token-expire-time}") Integer maxAge
    ) {
        this.kakaoUtils = kakaoUtils;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.maxAge = maxAge;
    }

    public void doLoginProcess(String code, HttpServletResponse response) {
        String accessToken = kakaoUtils.getAccessToken(code);

        KakaoUserInfo userInfo = kakaoUtils.getUserInfo(accessToken);

        User user = getUserIfNotExistsSignup(userInfo.getEmail(), userInfo.getName());

        String token = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + token);
        user.updateRefreshToken(refreshToken);
        setRefreshToken(refreshToken, response);
    }

    private User getUserIfNotExistsSignup(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> userService.save(email, name));
    }

    private void setRefreshToken(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }
}
