package epdbackendjava.chatassistant.Chat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Exchanger;

@Service
public class SessionService {


    private final SessionRepository sessionRepository;
    private final RestTemplate restTemplate;



    @Autowired
    public SessionService(SessionRepository sessionRepository, RestTemplate restTemplate) {
        this.sessionRepository = sessionRepository;
        this.restTemplate = restTemplate;
    }

    public Session createSession(Session session) {
        sessionRepository.save(session);
        return session;
    }

    public Session chatToBot(String sessionId, String input) {

        Session session = sessionRepository.findBySessionId(sessionId);
        String botMessage = "";

        if (session != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-RapidAPI-Key", "ea8ab30eadmshe895b9cbb8a476fp1ea342jsnff70dd151c87");
            headers.set("X-RapidAPI-Host", "lemurbot.p.rapidapi.com");

            String requestBody = "{\"bot\":\"dilly\",\"client\":\"d531e3bd-b6c3-4f3f-bb58-a6632cbed5e2\",\"message\":\"" + input + "\"}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "https://lemurbot.p.rapidapi.com/chat",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();

                // parsing JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(responseBody);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                botMessage = jsonNode.path("data").path("conversation").path("output").asText();

            }
            else {
                botMessage = "Sorry, couldn't find an answer";
            }

            Message userMessage = new Message(input, true, new Date());
            Message assistantMessage = new Message(botMessage, false, new Date());

            List<Message> conversations = session.getConversations();

            conversations.add(userMessage);
            conversations.add(assistantMessage);

            session.setConversations(conversations);

            sessionRepository.save(session);


        }

        return session;
    }

    public List<Session> getAllSessions(String userId) {
        return sessionRepository.findAllSessionsByUserId(userId);
    }

    public Optional<Session> getSessionById(String sessionId) {
        return sessionRepository.findById(sessionId);
    }




}
