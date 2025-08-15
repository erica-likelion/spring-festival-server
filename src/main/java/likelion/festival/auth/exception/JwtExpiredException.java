package likelion.festival.auth.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message) {
        super(message);
    }
}
