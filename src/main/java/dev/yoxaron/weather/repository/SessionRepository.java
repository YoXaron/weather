package dev.yoxaron.weather.repository;

import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    List<Session> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt <= :now")
    int deleteExpiredSessions(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Session s SET s.expiresAt = :now WHERE s.user = :user AND s.expiresAt > :now")
    void invalidateAllUserSessions(@Param("user") User user, @Param("now") LocalDateTime now);
}
