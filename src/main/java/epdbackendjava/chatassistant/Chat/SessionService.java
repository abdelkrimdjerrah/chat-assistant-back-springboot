package epdbackendjava.chatassistant.Chat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Exchanger;

@Service
public class SessionService {


    @Value("${GOOGLE_API_KEY}")
    String googleApiKey;
    private final SessionRepository sessionRepository;
    private final RestTemplate restTemplate;



    @Autowired
    public SessionService(SessionRepository sessionRepository, RestTemplate restTemplate) {
        this.sessionRepository = sessionRepository;
        this.restTemplate = restTemplate;
    }

    public String createSession(String userId) {
        Session session = new Session(userId);
        sessionRepository.save(session);
        return session.getSessionId();
    }

    public String chatToBot(String sessionId, String input) {

        System.out.println("ID");
        System.out.println(sessionId);
        System.out.println("ID");
        Session session = sessionRepository.findBySessionId(sessionId);
        String botMessage = "";

        System.out.println("1");


        if (session != null) {

            System.out.println("2");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"prompt\":{\"text\":\""+ input +"\"}}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            //Using PaLM API from google
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText?key="+googleApiKey,
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

                botMessage = jsonNode.path("candidates").get(0).path("output").asText();


            }
            else {
                System.out.println("4");
                botMessage = "Sorry, couldn't find an answer";
            }

            Message userMessage = new Message(input, true, new Date());
            Message assistantMessage = new Message(botMessage, false, new Date());

            List<Message> conversation = session.getConversation();

            conversation.add(userMessage);
            conversation.add(assistantMessage);

            session.setConversation(conversation);

            sessionRepository.save(session);

            System.out.println("5");


        }

        return botMessage;
    }

    public List<Session> getAllSessions(String userId) {
        return sessionRepository.findAllSessionsByUserId(userId);
    }

    public Optional<Session> getSessionById(String sessionId) {
        try {
            Optional<Session> t = sessionRepository.findById(sessionId);
            System.out.println("ggg");
            return t;
        } catch (Exception e) {
            System.out.println("olalalal");
            e.printStackTrace();
            // Handle the exception appropriately
            return Optional.empty(); // Or rethrow the exception
        }
    }

    public Boolean deleteSessionById(String sessionId) {
            sessionRepository.deleteById(sessionId);
            return sessionRepository.findById(sessionId).isEmpty();
    }

}
