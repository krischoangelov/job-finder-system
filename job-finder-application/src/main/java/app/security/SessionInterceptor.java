package app.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private static final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/", "/login", "/register", "/error");

    private static final Set<String> PUBLIC_PREFIXES = Set.of("/css/", "/js/", "/images/");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String currEndpoint = request.getServletPath();

        for (String prefix : PUBLIC_PREFIXES) {
            if (currEndpoint.startsWith(prefix)) {
                return true;
            }
        }

        HttpSession httpSession = request.getSession(false);

        if (UNAUTHENTICATED_ENDPOINTS.contains(currEndpoint)) {
            if (httpSession != null && httpSession.getAttribute("user_id") != null) {
                response.sendRedirect("/home");
                return false;
            }

            return true;
        }

        if (httpSession == null) {
            response.sendRedirect("/login");
            return false;
        }

        UUID userId = (UUID) httpSession.getAttribute("user_id");

        if (userId == null) {
            httpSession.invalidate();
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
