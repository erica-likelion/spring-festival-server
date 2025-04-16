package likelion.festival.config;

import likelion.festival.domain.User;
import likelion.festival.exceptions.UserNotFoundException;
import likelion.festival.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = userService.getUserByEmail(email);
        return new CustomUserDetails(user);
    }
}
