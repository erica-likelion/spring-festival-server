package likelion.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.enums.RoleType;
import likelion.festival.repository.UserRepository;
import likelion.festival.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private User testUser;
    private String refreshToken;
    private Pub testPub;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("test")
                .email("test@test.com")
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(testUser);

        refreshToken = jwtTokenUtils.generateRefreshToken(testUser);
        testUser.updateRefreshToken(refreshToken);
    }

    @Test
    void testRefreshTokenApi() throws Exception {
        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(content().string("Token refreshed"));
    }

    void testAdminLogin() throws Exception {

    }
}
