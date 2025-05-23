package likelion.festival.service;

import likelion.festival.domain.ConcertAlarmRequest;
import likelion.festival.domain.User;
import likelion.festival.enums.RoleType;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.JwtTokenException;
import likelion.festival.exceptions.UserNotFoundException;
import likelion.festival.repository.ConcertAlarmRequestRepository;
import likelion.festival.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ConcertAlarmRequestRepository concertAlarmRequestRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Connot find user with given id"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Connot find user with given email:" + email));
    }

    @Transactional
    public User save(String email, String name) {
        return userRepository.save(
                User.builder()
                        .name(name)
                        .email(email)
                        .role(RoleType.ROLE_USER)
                        .build()
        );
    }

    @Transactional
    public void setUserConcertAlarm(User user, String artistName) {
        concertAlarmRequestRepository.save(new ConcertAlarmRequest(artistName, user));
    }

    @Transactional
    public void deleteUserConcertAlarm(User user, String artistName) {
        ConcertAlarmRequest concertAlarmRequest = concertAlarmRequestRepository.findByArtistNameAndUser(artistName, user).orElseThrow(
                () -> new EntityNotFoundException("Cannot find concert alarm request with given artistName:" + artistName));

        concertAlarmRequestRepository.delete(concertAlarmRequest);
    }

    public User getUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new JwtTokenException("Refresh token not found"));
    }
}
