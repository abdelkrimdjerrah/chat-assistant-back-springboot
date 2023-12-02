package epdbackendjava.chatassistant.Chat;

import epdbackendjava.chatassistant.Config.JwtAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//@CrossOrigin("*")
@RestController
@RequestMapping("api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SessionController(SessionService sessionService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.sessionService = sessionService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //filter for JWT authentication to all endpoints in this controller
//    @ModelAttribute
//    public void addJwtFilterToAllEndpoints(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, @RequestHeader("Authorization") String authHeader) throws Exception {
//        jwtAuthenticationFilter.performAuthentication(request, response, filterChain);
//    }

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
    @DeleteMapping("/{sessionId}")
    public Boolean deleteSessionById(@PathVariable String sessionId) {
        return sessionService.deleteSessionById(sessionId);
    }




}
