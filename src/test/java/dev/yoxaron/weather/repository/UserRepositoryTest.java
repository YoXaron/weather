package dev.yoxaron.weather.repository;

import dev.yoxaron.weather.config.TestConfig;
import dev.yoxaron.weather.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = User.builder()
                .login("test_user")
                .password("secret")
                .build();

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = User.builder()
                .login("user1")
                .password("password1")
                .build();

        User user2 = User.builder()
                .login("user2")
                .password("password2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void shouldFindUserByLogin() {
        User user = User.builder()
                .login("find_me")
                .password("secret")
                .build();

        userRepository.saveAndFlush(user);

        Optional<User> found = userRepository.findByLogin("find_me");

        assertTrue(found.isPresent());
        assertThat(found.get().getLogin()).isEqualTo("find_me");
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> found = userRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFailOnDuplicateLogin() {
        User user1 = User.builder()
                .login("dup")
                .password("p1")
                .build();

        User user2 = User.builder()
                .login("dup")
                .password("p2")
                .build();

        userRepository.saveAndFlush(user1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    void shouldFailWhenLoginIsNull() {
        User user = User.builder()
                .password("secret")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    void shouldUpdateUser() {
        User testUser = User.builder()
                .login("test_user")
                .password("p1")
                .build();

        User saved = userRepository.save(testUser);
        Long userId = saved.getId();

        saved.setPassword("new_hashed_password");
        userRepository.save(saved);

        Optional<User> updated = userRepository.findById(userId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getPassword()).isEqualTo("new_hashed_password");
        assertThat(updated.get().getLogin()).isEqualTo("test_user");
    }

    @Test
    void shouldDeleteUserById() {
        User testUser = User.builder()
                .login("test_user")
                .password("p1")
                .build();

        User saved = userRepository.save(testUser);
        Long userId = saved.getId();

        userRepository.deleteById(userId);

        Optional<User> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }
}