package likelion.festival.auth.exception;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }
}
