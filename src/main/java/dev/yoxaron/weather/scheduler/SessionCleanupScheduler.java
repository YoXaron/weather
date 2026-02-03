package dev.yoxaron.weather.scheduler;

import dev.yoxaron.weather.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCleanupScheduler {

    private final SessionService sessionService;

    @Scheduled(cron = "0 * * * * *")
    public void cleanupExpiredSessions() {
        log.info("Starting scheduled task to clean up expired sessions");

        try {
            int deletedCount = sessionService.cleanupExpiredSessions();
            log.info("Scheduled task completed. Sessions removed: {}", deletedCount);
        } catch (Exception e) {
            log.error("Error while executing scheduled session cleanup task", e);
        }
    }
}
