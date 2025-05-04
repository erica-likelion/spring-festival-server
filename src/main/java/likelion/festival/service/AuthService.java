package likelion.festival.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.dto.KakaoUserInfo;
import likelion.festival.repository.PubRepository;
import likelion.festival.repository.UserRepository;
import likelion.festival.utils.JwtTokenUtils;
import likelion.festival.utils.KakaoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.RuntimeMBeanException;

@Service
@Slf4j
public class AuthService {
    private final KakaoUtils kakaoUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final Integer maxAge;
    private final PubRepository pubRepository;

    public AuthService(
            KakaoUtils kakaoUtils,
            UserRepository userRepository,
            UserService userService,
            JwtTokenUtils jwtTokenUtils,
            @Value("${jwt.refresh-token-expire-time}") Integer maxAge,
            PubRepository pubRepository
    ) {
        this.kakaoUtils = kakaoUtils;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.maxAge = maxAge;
        this.pubRepository = pubRepository;
    }

    @Transactional
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

    @Transactional
    public void adminLogin(String username, String password, HttpServletResponse response) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정을 요구하고 있습니다."));

        Pub pub = pubRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("찾고 있는 주점이 없습니다."));

        if (!pub.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + token);
        user.updateRefreshToken(refreshToken);
        setRefreshToken(refreshToken, response);
    }
}
