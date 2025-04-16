package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakaoAccount;


    @Getter
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @AllArgsConstructor
        public static class Profile {
            private String nickname;
        }
    }

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public String getName() {
        return kakaoAccount.getProfile().getNickname();
    }
}
