package likelion.festival.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.festival.exceptions.JwtAuthenticationException;
import likelion.festival.exceptions.JwtTokenException;
import likelion.festival.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;
    String[] WHITELIST = SecurityConfig.WHITELIST;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        for (String string : WHITELIST) {
            if (path.startsWith(string)) {
                if (path.equals("/api/lost-items") && request.getMethod().equals("POST")) {
                    break;
                }

                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            String token = resolveToken(request);

            jwtTokenUtils.validateToken(token);

            String email = (String) jwtTokenUtils.getClaims(token).get("Email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(authentication);


            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(ex.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        //log.info(request.getRequestURI());
        //log.info(request.getMethod());
        throw new JwtAuthenticationException("토큰이 존재하지 않거나, 잘 못 된 형식입니다.");
    }
}

