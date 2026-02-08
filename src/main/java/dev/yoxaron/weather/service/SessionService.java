package dev.yoxaron.weather.service;

import dev.yoxaron.weather.exception.SessionExpiredException;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.model.User;
import dev.yoxaron.weather.repository.SessionRepository;
import dev.yoxaron.weather.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static dev.yoxaron.weather.utils.SessionUtil.isExpired;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public Session createSession(User user) {
        log.info("Creating new session for user: {}", user.getLogin());

        invalidateAllUserSessions(user);

        Session session = Session.builder()
                .user(user)
                .expiresAt(SessionUtil.calculateExpirationTime())
                .build();

        Session savedSession = sessionRepository.save(session);
        log.info("Session created with ID: {} for user: {}", savedSession.getId(), user.getLogin());

        return savedSession;
    }

    @Transactional(readOnly = true)
    public Optional<Session> findActiveSession(UUID sessionId) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);

        if (sessionOpt.isEmpty()) {
            return Optional.empty();
        }

        Session session = sessionOpt.get();

        if (isExpired(session.getExpiresAt())) {
            log.warn("Attempt to use expired session: {}", sessionId);
            throw new SessionExpiredException("Session has expired. Please log in again.");
        }

        return Optional.of(session);
    }

    @Transactional
    public void invalidateSession(UUID sessionId) {
        log.info("Invalidating session: {}", sessionId);

        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.setExpiresAt(LocalDateTime.now());
            sessionRepository.save(session);
            log.info("Session {} successfully invalidated", sessionId);
        });
    }

    public void invalidateAllUserSessions(User user) {
        log.info("Invalidating all active sessions for user: {}", user.getLogin());
        sessionRepository.invalidateAllUserSessions(user, LocalDateTime.now());
    }

    @Transactional
    public int cleanupExpiredSessions() {
        log.info("Starting cleanup of expired sessions");
        int deletedCount = sessionRepository.deleteExpiredSessions(LocalDateTime.now());
        log.info("Expired sessions removed: {}", deletedCount);
        return deletedCount;
    }
}
