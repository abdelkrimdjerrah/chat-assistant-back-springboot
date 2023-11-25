package epdbackendjava.chatassistant.Chat;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public Session createSession(Session session) {
        return sessionService.createSession(session);
    }

    @PostMapping("/chat")
    public Session chatToBot(@RequestBody Map<String, String> requestBody) {
        String sessionId = requestBody.get("sessionId");
        String input = requestBody.get("input");
        return sessionService.chatToBot(sessionId, input);
    }

    @GetMapping("/{userId}")
    public List<Session> getAllSessions(@PathVariable String userId) {
        return sessionService.getAllSessions(userId);
    }

    @GetMapping("/{userId}/{sessionId}")
    public Optional<Session> getSessionById(@PathVariable String sessionId) {
        return sessionService.getSessionById(sessionId);
    }


}
