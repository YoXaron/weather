package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.config.TestConfig;
import dev.yoxaron.weather.config.WebConfig;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.model.User;
import dev.yoxaron.weather.repository.SessionRepository;
import dev.yoxaron.weather.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebConfig.class})
@WebAppConfiguration
@Transactional
class AuthControllerIT {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private SessionRepository sessionRepository;
//
//    private MockMvc mockMvc;
//    private BCryptPasswordEncoder passwordEncoder;
//    private User testUser;
//    private Session testSession;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        passwordEncoder = new BCryptPasswordEncoder();
//
//        sessionRepository.deleteAll();
//        userRepository.deleteAll();
//
//        // Create test user
//        testUser = new User();
//        testUser.setLogin("testuser");
//        testUser.setPassword(passwordEncoder.encode("Password123!"));
//        testUser = userRepository.save(testUser);
//
//        // Create test session
//        testSession = new Session();
//        testSession.setId(UUID.randomUUID());
//        testSession.setUser(testUser);
//        testSession.setExpiresAt(LocalDateTime.now().plusHours(1));
//        testSession = sessionRepository.save(testSession);
//    }
//
//    @Test
//    void showSignUpPage_ReturnsSignUpView() throws Exception {
//        mockMvc.perform(get("/auth/signup"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("sign-up"))
//                .andExpect(model().attributeExists("signUpRequest"));
//    }
}
