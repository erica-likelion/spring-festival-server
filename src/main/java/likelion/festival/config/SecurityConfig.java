package likelion.festival.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final List<WhitelistEntry> WHITELIST = List.of(
            new WhitelistEntry(HttpMethod.GET, "/auth/login/kakao/auth-code"),
            new WhitelistEntry(HttpMethod.GET, "/error"),
            new WhitelistEntry(HttpMethod.POST, "/auth/refresh"),
            new WhitelistEntry(HttpMethod.POST, "/auth/admin-login"),
            new WhitelistEntry(HttpMethod.GET, "/api/lost-items/**"),
            new WhitelistEntry(HttpMethod.GET, "/api/pubs"),
            new WhitelistEntry(HttpMethod.POST, "/api/pubs/like"),
            new WhitelistEntry(HttpMethod.GET, "/images/**")
    );

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    for (WhitelistEntry entry : WHITELIST) {
                        requests.requestMatchers(entry.getMethod(), entry.getPath()).permitAll();
                    }
                    requests.anyRequest().authenticated();
                })
                .cors(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(CorsConfig.corsConfigurationSource())
                )
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
