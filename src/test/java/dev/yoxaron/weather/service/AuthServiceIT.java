package dev.yoxaron.weather.service;

import dev.yoxaron.weather.dto.UserSignInRequestDto;
import dev.yoxaron.weather.dto.UserSignUpRequestDto;
import dev.yoxaron.weather.exception.AuthException;
import dev.yoxaron.weather.exception.UserAlreadyExistsException;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceIT extends AbstractIT {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    private static final String DEFAULT_LOGIN = "testuser";
    private static final String DEFAULT_PASSWORD = "testpass";

    @Test
    void shouldRegisterUser() {
        UserSignUpRequestDto dto = createSignUpDto(DEFAULT_LOGIN, DEFAULT_PASSWORD);

        authService.register(dto);

        assertThat(userRepository.findByLogin(DEFAULT_LOGIN)).isPresent();
    }

    @Test
    void shouldThrowUserAlreadyExistsException() {
        UserSignUpRequestDto dto = createSignUpDto(DEFAULT_LOGIN, DEFAULT_PASSWORD);

        authService.register(dto);

        assertThat(userRepository.findByLogin(DEFAULT_LOGIN)).isPresent();

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(dto));
    }

    @Test
    void shouldAuthenticateUser() {
        UserSignUpRequestDto registerDto = createSignUpDto(DEFAULT_LOGIN, DEFAULT_PASSWORD);

        authService.register(registerDto);

        UserSignInRequestDto dto = createSignInDto(DEFAULT_LOGIN, DEFAULT_PASSWORD);

        Session session = authService.authenticate(dto);

        assertThat(session).isNotNull();
        assertThat(session.getUser()).isNotNull();
        assertThat(session.getUser().getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(session.getExpiresAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldThrowExceptionWhenAuthenticatingNonExistentUser() {
        UserSignInRequestDto dto = createSignInDto("nonexistent", DEFAULT_PASSWORD);

        assertThrows(AuthException.class, () -> authService.authenticate(dto));
    }

    private UserSignUpRequestDto createSignUpDto(String login, String password) {
        return UserSignUpRequestDto.builder()
                .login(login)
                .password(password)
                .build();
    }

    private UserSignInRequestDto createSignInDto(String login, String password) {
        return UserSignInRequestDto.builder()
                .login(login)
                .password(password)
                .build();
    }
}
