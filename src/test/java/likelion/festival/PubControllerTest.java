package likelion.festival;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.dto.PubRequestDto;
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
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PubControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Pub testPub;
    private Long pubId;

    @BeforeEach
    @Commit
    void setUp() {
        String uniqueName = "test_" + UUID.randomUUID();
        testPub = Pub.builder()
                .name(uniqueName)
                .enterNum(3)
                .likeCount(0L)
                .maxWaitingNum(10)
                .build();
        pubRepository.save(testPub);
        pubRepository.flush();
        pubId = testPub.getId();

        System.out.println("pubId is :" + pubId);
    }

    @Test
    void testAddLikeWithConcurrentIssue() throws Exception {
        PubRequestDto requestDto = new PubRequestDto(10L);

        int threadCount = 10;
        int iterations = 30;

        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                try {
                    mockMvc.perform(post("/api/pubs/" + pubId.intValue() + "/likes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            latch.countDown();
        };

        for (int i = 0; i < threadCount; i++) {
            executor.execute(task);
        }

        latch.await();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Pub pub = pubRepository.findById(pubId).orElseThrow();
        assertThat(pub.getLikeCount()).isEqualTo(3000L);
        System.out.println("total like count is : " + pub.getLikeCount());
    }

    @Test
    void testAddLike() throws Exception {
        PubRequestDto requestDto = new PubRequestDto(10L);
        mockMvc.perform(post("/api/pubs/" + pubId.intValue() + "/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        assertThat(testPub.getLikeCount()).isEqualTo(10L);
    }
}
