package epdbackendjava.chatassistant.Chat;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public String createSession(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        return sessionService.createSession(userId);
    }

    @PostMapping("/chat")
    public String chatToBot(@RequestBody Map<String, String> requestBody) {
        String sessionId = requestBody.get("sessionId");
        String input = requestBody.get("input");
        return sessionService.chatToBot(sessionId, input);
    }

    @GetMapping("/users/{userId}")
    public List<Session> getAllSessions(@PathVariable String userId) {
        return sessionService.getAllSessions(userId);
    }

    @GetMapping("/{sessionId}")
    public Optional<Session> getSessionById(@PathVariable String sessionId) {
        return sessionService.getSessionById(sessionId);
    }


}
