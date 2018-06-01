package ml.echelon133.controller;

import ml.echelon133.model.message.InMessage;
import ml.echelon133.model.message.OutMessage;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class RoomMessageController {

    @MessageMapping("/{roomName}")
    @SendTo("/topic/{roomName}")
    public OutMessage roomMessageHandler(@DestinationVariable String roomName, OAuth2AuthenticationToken token,
                                         InMessage inMessage) {
        Map<String, Object> userAttributes = token.getPrincipal().getAttributes();
        String username = userAttributes.get("login").toString();
        String avatarUrl = userAttributes.get("avatar_url").toString();
        String profileUrl = userAttributes.get("html_url").toString();
        return new OutMessage(username, avatarUrl, profileUrl, inMessage.getMessageContent(), inMessage.getType());
    }

    @MessageExceptionHandler
    public void conversionExceptionHandler(MessageConversionException ex) {
        // Do nothing - if this error happens it means that the user modified the client to send messages that cannot be
        // deserialized - probably message type was changed to a type that does not appear in MessageType enum
    }
}
