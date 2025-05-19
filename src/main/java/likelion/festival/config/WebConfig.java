package likelion.festival.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://spring-festival-testing.vercel.app",
                        "https://spring-festival-admin.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                //.exposedHeaders("Authorization") // 프론트에서 읽을 수 있게
                .allowCredentials(true); // 쿠키 허용
    }
}
