package likelion.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.enums.RoleType;
import likelion.festival.repository.PubRepository;
import likelion.festival.repository.UserRepository;
import likelion.festival.repository.WaitingRepository;
import likelion.festival.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private WaitingRepository waitingRepository;

    private String token;
    private User testUser;
    private Pub testPub;
    private Long pubId;
    private Waiting waiting;
    private Waiting waiting2;
    private Waiting waiting3;
    private Long waitingId;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("test")
                .email("test@test.com")
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(testUser);

        testPub = Pub.builder()
                .name("test")
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
                .andExpect(jsonPath("$.waitingNum").value(11))
                .andExpect(jsonPath("$.numsTeamsAhead").value(8));
    }

    void initWaiting() {
        waiting = new Waiting(3, "010-1234-5678", "test", 12, testUser, testPub);
        waitingRepository.save(waiting);
        waitingId = waiting.getId();
    }

    @Test
    void testDeleteWaiting() throws Exception {
        initWaiting();
        mockMvc.perform(delete("/api/waitings").param("id", String.valueOf(waitingId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteWaitingByWrongId() throws Exception {
        mockMvc.perform(delete("/api/waitings").param("id", String.valueOf(1000999))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

}
