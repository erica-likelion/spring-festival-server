package likelion.festival.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakao_account;
    private Properties properties;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoAccount {
        private String email;
        private Profile profile;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {
        private String nickname;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
    }

    public String getEmail() {
        return kakao_account.getEmail();
    }

    public String getName() {
        return kakao_account.getProfile().getNickname();
    }
}