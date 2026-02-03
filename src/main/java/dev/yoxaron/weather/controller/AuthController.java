package dev.yoxaron.weather.controller;

import dev.yoxaron.weather.dto.UserSignInRequestDto;
import dev.yoxaron.weather.dto.UserSignUpRequestDto;
import dev.yoxaron.weather.exception.AuthException;
import dev.yoxaron.weather.exception.UserAlreadyExistsException;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.service.AuthService;
import dev.yoxaron.weather.service.SessionService;
import dev.yoxaron.weather.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    public static final String SIGN_UP_PAGE = "sign-up";
    public static final String SIGN_IN_PAGE = "sign-in";

    private final AuthService authService;
    private final SessionService sessionService;

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpRequest", new UserSignUpRequestDto());
        return SIGN_UP_PAGE;
    }

    @PostMapping("/signup")
    public String signUp(
            @Valid @ModelAttribute("signUpRequest") UserSignUpRequestDto userSignUpRequestDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return SIGN_UP_PAGE;
        }

        try {
            authService.register(userSignUpRequestDto);
            return "redirect:/auth/signin";

        } catch (UserAlreadyExistsException e) {
            log.warn("Registration error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return SIGN_UP_PAGE;
        }
    }

    @GetMapping("/signin")
    public String showSignInPage(Model model) {
        model.addAttribute("signInRequest", new UserSignInRequestDto());
        return SIGN_IN_PAGE;
    }

    @PostMapping("/signin")
    public String signIn(
            @Valid @ModelAttribute("signInRequest") UserSignInRequestDto userSignInRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return SIGN_IN_PAGE;
        }

        try {
            Session session = authService.authenticate(userSignInRequestDto);
            CookieUtil.addSessionCookie(response, session.getId());

            log.info("User {} successfully logged in", userSignInRequestDto.getLogin());
            return "redirect:/home";

        } catch (AuthException e) {
            log.warn("Authentication error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return SIGN_IN_PAGE;
        }
    }

    @GetMapping("/signout")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.getSessionIdFromCookie(request).ifPresent(sessionId -> {
            sessionService.invalidateSession(sessionId);
            log.info("User logged out, session: {}", sessionId);
        });

        CookieUtil.deleteSessionCookie(response);

        return "redirect:/auth/signin";
    }
}
