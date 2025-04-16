package likelion.festival.utils;

import likelion.festival.dto.KakaoUserInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Getter
public class KakaoUtils {
    @Value("${kakao.auth.client}")
    private String clientId;
    @Value("${kakao.auth.redirect}")
    private String redirectUri;
    @Value("${kakao.auth.token-uri}")
    private String tokenUri;
    @Value("${kakao.auth.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient = WebClient.builder().build();

    public String getAccessToken(String code) {
        return webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(Map.class)
                .block()
                .get("access_token")
                .toString();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }
}
