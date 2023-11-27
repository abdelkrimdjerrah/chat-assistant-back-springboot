package epdbackendjava.chatassistant.Chat;

import java.util.Date;

public class Message {
    private String text;
    private boolean isUser;
    private Date timestamp;

    public Message(String text, boolean isUser, Date timestamp) {
        this.text = text;
        this.isUser = isUser;
        this.timestamp = timestamp;
    }

    public String getText() {return text;}

    public void setText(String text) {
        this.text = text;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
