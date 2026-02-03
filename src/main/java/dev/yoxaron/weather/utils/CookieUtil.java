package dev.yoxaron.weather.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class CookieUtil {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    private static final int SESSION_COOKIE_MAX_AGE = 24 * 60 * 60;

    public static void addSessionCookie(HttpServletResponse response, UUID sessionId) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId.toString());
        cookie.setMaxAge(SESSION_COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static long getCurrentUserIdFromRequest(HttpServletRequest request) {
        return (long) request.getAttribute("currentUserId");
    }

    public static Optional<UUID> getSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .flatMap(CookieUtil::parseUUID);
    }

    public static void deleteSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private static Optional<UUID> parseUUID(String uuidString) {
        try {
            return Optional.of(UUID.fromString(uuidString));
        } catch (IllegalArgumentException _) {
            return Optional.empty();
        }
    }
}
