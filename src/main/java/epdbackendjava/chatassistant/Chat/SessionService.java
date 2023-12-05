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


        Session session = sessionRepository.findBySessionId(sessionId);
        String botMessage = "";



        if (session != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            StringBuilder fullConversationText = new StringBuilder();
            session.getConversation().forEach(message -> {

                if (message.isUser()) {
                    fullConversationText.append(" Friend:").append(message.getText());
                } else {
                    fullConversationText.append(" Ines:").append(message.getText());
                }
            });


            StringBuilder templateRequestBody = new StringBuilder();
            templateRequestBody.append("You are Ines, this is your conversation with your Friend: (").append(fullConversationText).append(") now your Friend asked you a question : ").append(input).append(". Give him the answer as Ines.");



            String requestBody = "{\"prompt\":{\"text\":\""+ templateRequestBody +"\"}}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            //Using ≈ API from google
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

                if (jsonNode != null && jsonNode.has("candidates") && jsonNode.get("candidates").isArray() && jsonNode.get("candidates").size() > 0) {
                    JsonNode firstCandidate = jsonNode.get("candidates").get(0);
                    if (firstCandidate.has("output")) {
                        botMessage = firstCandidate.get("output").asText();
                    } else {
                        botMessage = "Sorry, couldn't find an answer";
                    }
                } else {
                    botMessage = "Sorry, couldn't find an answer";
                }


            }
            else {

                botMessage = "Sorry, couldn't find an answer";
            }

            Message userMessage = new Message(input, true, new Date());
            Message assistantMessage = new Message(botMessage, false, new Date());

            List<Message> conversation = session.getConversation();

            conversation.add(userMessage);
            conversation.add(assistantMessage);

            session.setConversation(conversation);

            sessionRepository.save(session);



        }

        return botMessage;
    }

    public List<Session> getAllSessions(String userId) {
        return sessionRepository.findAllSessionsByUserId(userId);
    }

    public Optional<Session> getSessionById(String sessionId) {
        try {
            Optional<Session> t = sessionRepository.findById(sessionId);
            return t;
        } catch (Exception e) {
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
