package dev.yoxaron.weather.service;

import dev.yoxaron.weather.dto.UserSignInRequestDto;
import dev.yoxaron.weather.dto.UserSignUpRequestDto;
import dev.yoxaron.weather.exception.AuthException;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public void register(UserSignUpRequestDto userSignUpRequestDto) {
        log.info("Attempting to register user with login: {}", userSignUpRequestDto.getLogin());

        userService.register(userSignUpRequestDto);
    }

    @Transactional
    public Session authenticate(UserSignInRequestDto userSignInRequestDto) {
        log.info("Attempting to authenticate user: {}", userSignInRequestDto.getLogin());

        User user = userService.findByLogin(userSignInRequestDto.getLogin())
                .orElseThrow(() -> {
                    log.warn("User with login {} not found", userSignInRequestDto.getLogin());
                    return new AuthException("Invalid login or password");
                });

        if (!userService.checkPassword(user, userSignInRequestDto.getPassword())) {
            log.warn("Invalid password for user: {}", userSignInRequestDto.getLogin());
            throw new AuthException("Invalid login or password");
        }

        log.info("User {} successfully authenticated", userSignInRequestDto.getLogin());
        return sessionService.createSession(user);
    }
}
