package dev.yoxaron.weather.repository;

import dev.yoxaron.weather.model.Location;
import dev.yoxaron.weather.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUser(User user);

    @Modifying
    @Query("""
            DELETE FROM Location l
            WHERE l.user.id = :userId
              AND ABS(l.latitude - :latitude) < 0.002
              AND ABS(l.longitude - :longitude) < 0.002
            """)
    void deleteByUserUsernameAndCoordinates(
            @Param("userId") Long userId,
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude
    );
}
