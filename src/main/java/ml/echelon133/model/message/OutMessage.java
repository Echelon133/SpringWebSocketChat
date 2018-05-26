package ml.echelon133.model.message;

import org.springframework.web.util.HtmlUtils;

import java.time.LocalTime;

public class OutMessage {

    private String username;
    private String content;
    private LocalTime time;
    private String avatarUrl;
    private String profileUrl;
    private MessageType type;

    public OutMessage() {}
    public OutMessage(String username, String avatarUrl, String profileUrl, String content, MessageType type) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.profileUrl = profileUrl;
        this.content = HtmlUtils.htmlEscape(content);
        this.time = LocalTime.now();
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}