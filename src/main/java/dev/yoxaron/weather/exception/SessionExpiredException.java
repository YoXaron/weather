package dev.yoxaron.weather.exception;

public class SessionExpiredException extends AuthException {
    public SessionExpiredException(String message) {
        super(message);
    }
}
