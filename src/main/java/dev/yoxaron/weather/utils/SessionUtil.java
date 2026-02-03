package dev.yoxaron.weather.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class SessionUtil {

    private static final int SESSION_DURATION_HOURS = 24;

    public static LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plusHours(SESSION_DURATION_HOURS);
    }

    public static boolean isExpired(LocalDateTime expiresAt) {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
