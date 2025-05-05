package likelion.festival.waiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.dto.WaitingRequestDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WaitingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private User testUser;
    private Pub testPub;
    private Long pubId;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("test")
                .email("test@test.com")
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(testUser);

        testPub = Pub.builder()
                .host("test")
                .enterNum(3)
                .likeCount(1000L)
                .maxWaitingNum(10)
                .build();
        pubRepository.save(testPub);
        pubId = testPub.getId();

        token = jwtTokenUtils.generateAccessToken(testUser);
    }

    @Test
    void testMakeWaiting() throws Exception {
        WaitingRequestDto requestDto = new WaitingRequestDto(3, "010-1234-5678","hello", pubId);

        mockMvc.perform(post("/api/waitings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pubId))
                .andExpect(jsonPath("$.waitingNum").value(11))
                .andExpect(jsonPath("$.numsTeamsAhead").value(8));
    }
}
