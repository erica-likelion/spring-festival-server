package likelion.festival.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.exceptions.global.InvalidRequestException;
import likelion.festival.pub.domain.Pub;
import likelion.festival.pub.exception.PubException;
import likelion.festival.user.domain.User;
import likelion.festival.auth.dto.KakaoUserInfo;
import likelion.festival.pub.repository.PubRepository;
import likelion.festival.user.exception.UserNotFoundException;
import likelion.festival.user.repository.UserRepository;
import likelion.festival.auth.utils.JwtTokenUtils;
import likelion.festival.auth.utils.KakaoUtils;
import likelion.festival.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * @author 김승민, 송재현
 */
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

    /**
     * 카카오 인증 코드를 이용한 로그인 및 회원가입을 처리합니다.
     *
     * @param code 카카오 인증 서버로부터 받은 인증 코드
     * @param response HTTP 응답 객체 (토큰을 헤더와 쿠키에 추가하기 위해 사용)
     */
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

    /**
     * 이메일을 통해 사용자를 조회하고, 없으면 회원가입을 진행합니다.
     *
     * @param email 사용자 이메일
     * @param name 사용자 이름
     * @return 생성된 {@link User} 엔티티
     */
    private User getUserIfNotExistsSignup(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> userService.save(email, name));
    }

    /**
     * 리프레시 토큰을 HTTP 응답 쿠키에 설정합니다.
     *
     * @param refreshToken 발급된 리프레시 토큰
     * @param response HTTP 응답 객체
     */
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
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 계정을 요구하고 있습니다."));

        Pub pub = pubRepository.findByName(username)
                .orElseThrow(() -> new PubException("찾고 있는 주점이 없습니다."));

        if (!pub.getPassword().equals(password)) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);

        response.setHeader("Authorization", "Bearer " + token);
        user.updateRefreshToken(refreshToken);
        setRefreshToken(refreshToken, response);
    }
}
