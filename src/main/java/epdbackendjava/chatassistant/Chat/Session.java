package epdbackendjava.chatassistant.Chat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sessions")
public class Session {
    @Id
    private String sessionId;
    private String userId;
    private List<Message> conversations;

    public Session(String userId, List<Message> conversations) {
        this.userId = userId;
        this.conversations = conversations;
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

    public List<Message> getConversations() {
        return conversations;
    }

    public void setConversations(List<Message> conversations) {
        this.conversations = conversations;
    }

}
