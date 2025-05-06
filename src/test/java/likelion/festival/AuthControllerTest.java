package likelion.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.dto.AdminLoginDto;
import likelion.festival.enums.RoleType;
import likelion.festival.repository.PubRepository;
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

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    void initPub() {
        testPub = Pub.builder()
                .enterNum(3)
                .maxWaitingNum(10)
                .likeCount(1000L)
                .name("test@test.com")
                .password("arsenal")
                .build();

        pubRepository.save(testPub);
    }

    @Test
    void testAdminLogin() throws Exception {
        initPub();
        AdminLoginDto adminLoginDto = new AdminLoginDto("test@test.com", "arsenal");

        mockMvc.perform(post("/auth/admin-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLoginDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("refreshToken"));
    }
}
