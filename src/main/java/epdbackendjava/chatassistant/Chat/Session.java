package epdbackendjava.chatassistant.Chat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "sessions")
public class Session {
    @Id
    private String sessionId;
    private String userId;
    private List<Message> conversation;


    public Session(String userId, List<Message> conversation) {
        this.userId = userId;
        this.conversation = conversation;
    }

    public Session(String userId) {
        this.userId = userId;
        this.conversation = new ArrayList<>();
    }

    public Session() {
        this.userId = "";
        this.conversation = new ArrayList<>();
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }

}
