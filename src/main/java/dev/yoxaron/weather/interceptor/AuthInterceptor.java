package dev.yoxaron.weather.interceptor;

import dev.yoxaron.weather.exception.SessionExpiredException;
import dev.yoxaron.weather.model.Session;
import dev.yoxaron.weather.service.SessionService;
import dev.yoxaron.weather.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private static final String SIGN_IN_PATH = "/auth/signin";

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.debug("Authentication check for URI: {}", requestURI);

        Optional<UUID> sessionIdOpt = CookieUtil.getSessionIdFromCookie(request);

        if (sessionIdOpt.isEmpty()) {
            log.warn("Session cookie is missing");
            response.sendRedirect(request.getContextPath() + SIGN_IN_PATH);
            return false;
        }

        UUID sessionId = sessionIdOpt.get();

        try {
            Optional<Session> sessionOpt = sessionService.findActiveSession(sessionId);

            if (sessionOpt.isEmpty()) {
                log.warn("Active session not found: {}", sessionId);
                CookieUtil.deleteSessionCookie(response);
                response.sendRedirect(request.getContextPath() + SIGN_IN_PATH);
                return false;
            }

            Session session = sessionOpt.get();
            request.setAttribute("currentUserId", session.getUser().getId());
            request.setAttribute("username", session.getUser().getLogin());
            request.setAttribute("sessionId", sessionId);

            log.debug("User {} authenticated for URI: {}", session.getUser().getLogin(), requestURI);

            return true;

        } catch (SessionExpiredException _) {
            log.warn("Session expired: {}", sessionId);
            CookieUtil.deleteSessionCookie(response);
            response.sendRedirect(request.getContextPath() + SIGN_IN_PATH);
            return false;
        }
    }
}
