var messagesDiv = document.querySelector("#all-messages");
var messageForm = document.querySelector("#message-form");
var messageField = document.querySelector("#message");
var csrf = document.querySelector("meta[name='_csrf']").getAttribute('value');
var stompClient = null;
var roomName = document.querySelector("meta[name='roomName']").getAttribute('value');
var sentTo = "/msg/" + roomName;
var subscribeTo = "/topic/" + roomName;

function connect(event) {
    var socket = new SockJS("/spring-chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({"X-CSRF-TOKEN":csrf}, onConnectedHandler, onErrorHandler);
}

function onConnectedHandler() {
    stompClient.subscribe(subscribeTo, onMessageReceivedHandler);
    // Send initial message with MSG_JOIN type so the server can display "X join the room"
    stompClient.send(sendTo, {}, JSON.stringify({messageContent:"", type:"MSG_JOIN"}));
}

function onErrorHandler(error) {
    alert("There was an error with connecting to this room: " + error.toString());
}

function sendMessage(event) {
    var messageText = messageField.value.trim();

    if (messageText && stompClient) {
        var chatMessage = {
            messageContent: messageText,
            type: "MSG_CHAT"
        };

        stompClient.send(sendTo, {}, JSON.stringify(chatMessage));
        // After the message is sent, clear the input field for the next message
        messageField.value = '';
    }

    event.preventDefault();
}

function onMessageReceivedHandler(payload) {
    var messageReceived = JSON.parse(payload.body);
    var displayedMessage;
    var messageElement = document.createElement("div");

    messageElement.classList.add("row");

    if (messageReceived.type === "MSG_JOIN") {
        // mark message as an event (joined, left),
        messageElement.classList.add("event-message");
        var messageParagraph = document.createElement("p");
        displayedMessage = messageReceived.username + " joined this room.";

        var usernameJoinedText = document.createTextNode(displayedMessage);
        messageParagraph.appendChild(usernameJoinedText);
        messageElement.appendChild(messageParagraph);
    } else if (messageReceived.type === "MSG_LEAVE") {
        messageElement.classList.add("event-message");
        var messageParagraph = document.createElement("p");
        displayedMessage = messageReceived.username + " left this room.";

        var usernameLeftText = document.createTextNode(displayedMessage);
        messageParagraph.appendChild(usernameLeftText);
        messageElement.appendChild(messageParagraph);
    } else {
        messageElement.classList.add("chat-message");

        var timeElement = document.createElement("p");
        var timeText = document.createTextNode(messageReceived.time);
        timeElement.appendChild(timeText);

        var usernameElement = document.createElement("p");
        var usernameText = document.createTextNode(messageReceived.username);
        usernameElement.appendChild(usernameText);

        var messageBodyElement = document.createElement("p");
        var messageBodyText = document.createTextNode(messageReceived.messageContent);
        messageBodyElement.appendChild(messageBodyText);
    }

    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

connect();
messageForm.addEventListener("submit", sendMessage, true);