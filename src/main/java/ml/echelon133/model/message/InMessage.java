package ml.echelon133.model.message;

import org.hibernate.validator.constraints.Length;

public class InMessage {

    private MessageType type;

    @Length(min = 1, max = 150)
    private String messageContent;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
