package likelion.festival.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion.festival.domain.User;
import likelion.festival.exceptions.JwtTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {

    private final Key key;

    private long expirationTime;

    public JwtTokenUtils(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.expire-time}") long expirationTime) {
        this.expirationTime = expirationTime;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setIssuedAt(now)
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setHeader(createHeader())
                .setIssuedAt(now)
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtTokenException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new JwtTokenException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException("Invalid JWT token");
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Role", user.getRole());
        claims.put("Email", user.getEmail());
        claims.put("Name", user.getName());
        return claims;
    }
}
