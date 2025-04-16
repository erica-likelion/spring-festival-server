package likelion.festival.service;

import likelion.festival.domain.User;
import likelion.festival.enums.RoleType;
import likelion.festival.exceptions.UserNotFoundException;
import likelion.festival.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Connot find user with given id"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Connot find user with given email"));
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
}
