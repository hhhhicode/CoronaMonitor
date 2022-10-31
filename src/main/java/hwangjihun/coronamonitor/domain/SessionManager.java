package hwangjihun.coronamonitor.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

// 사용하지 않음

/*@Component
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRepository sessionRepository;
    public static final String SESSION_COOKIE_NAME = "userSessionId";

    public void createSession(Member value, HttpServletResponse response) {

        String sessionId = UUID.randomUUID().toString();

        UserSession userSession = new UserSession();
        userSession.setSessionId(sessionId);
        userSession.setValue(value);

        sessionRepository.save(userSession);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        // cookie: name = userSessionId, value = sessionId = UUID.randomUUID().toString()
        Optional<UserSession> userSession = sessionRepository.findById(sessionCookie.getValue());
        if (userSession.isEmpty()) return null;
        return userSession.get().getValue();
    }

    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionRepository.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request, String sessionCookieName) {

        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(sessionCookieName))
                .findAny()
                .orElse(null);
    }
}*/
