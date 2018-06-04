package ml.echelon133.model.message;

import org.springframework.web.util.HtmlUtils;

class MessageUtils {

    private static String replaceURLsWithLinks(String message) {
        return message.replaceAll("(http(s)?)(:\\/\\/)([^w{3}$]?)([a-zA-Z-]*)(\\.)(\\w+)(.+)", "<a href='$0'>$0</a>");
    }

    static String prepareMessage(String message) {
        // Escape HTML before replacing plain URLs with actual HTML links, otherwise links we have just created will break
        String escapedMessage = HtmlUtils.htmlEscape(message);
        return MessageUtils.replaceURLsWithLinks(escapedMessage);
    }
}
