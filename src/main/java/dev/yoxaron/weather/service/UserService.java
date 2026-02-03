package dev.yoxaron.weather.service;

import dev.yoxaron.weather.dto.UserSignUpRequestDto;
import dev.yoxaron.weather.exception.UserAlreadyExistsException;
import dev.yoxaron.weather.exception.UserDoesNotExistException;
import dev.yoxaron.weather.model.User;
import dev.yoxaron.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserSignUpRequestDto request) {
        log.info("Attempting to register user with login: {}", request.getLogin());

        if (userRepository.existsByLogin(request.getLogin())) {
            log.warn("User with login {} already exists", request.getLogin());
            throw new UserAlreadyExistsException("User with login " + request.getLogin() + " already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .login(request.getLogin())
                .password(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User successfully registered: {}", savedUser.getLogin());
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            throw new UserDoesNotExistException("User doesn't exist");
        }

        return userOpt.get();
    }
}
