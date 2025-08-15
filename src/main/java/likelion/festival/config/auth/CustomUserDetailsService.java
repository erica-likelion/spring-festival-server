package likelion.festival.config.auth;

import likelion.festival.user.domain.User;
import likelion.festival.user.exception.UserNotFoundException;
import likelion.festival.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = userService.getUserByEmail(email);
        return new CustomUserDetails(user);
    }
}
