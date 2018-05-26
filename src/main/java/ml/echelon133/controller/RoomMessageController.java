package ml.echelon133.controller;

import ml.echelon133.model.message.InMessage;
import ml.echelon133.model.message.MessageType;
import ml.echelon133.model.message.OutMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RoomMessageController {

    @MessageMapping("/{roomName}")
    @SendTo("/topic/{roomName}")
    public OutMessage roomMessageHandler(@DestinationVariable String roomName, OAuth2AuthenticationToken token,
                                         @Valid InMessage inMessage, BindingResult result) {
        if (result.hasErrors()) {
            // There is only one constraint on InMessage, so we assume that only error possible is invalid message length
            return new OutMessage("", "", "", "Invalid message length", MessageType.MSG_ERR);
        }

        Map<String, Object> userAttributes = token.getPrincipal().getAttributes();
        String username = userAttributes.get("login").toString();
        String avatarUrl = userAttributes.get("avatar_url").toString();
        String profileUrl = userAttributes.get("html_url").toString();
        return new OutMessage(username, avatarUrl, profileUrl, inMessage.getMessageContent(), inMessage.getType());
    }
}
