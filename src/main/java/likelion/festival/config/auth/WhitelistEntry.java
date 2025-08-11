package likelion.festival.config.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@AllArgsConstructor
public class WhitelistEntry {
    private HttpMethod method;
    private String path;
}
