var messagesDiv = document.querySelector("#all-messages");
var messageForm = document.querySelector("#message-form");
var messageField = document.querySelector("#message");
var disconnectButton = document.querySelector("#disconnect");
var csrf = document.querySelector("meta[name='_csrf']").getAttribute('value');
var stompClient = null;
var roomName = document.querySelector("meta[name='roomName']").getAttribute('value');
var sendTo = "/msg/" + roomName;
var subscribeTo = "/topic/" + roomName;

function connect(event) {
    var socket = new SockJS("/spring-chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({"X-CSRF-TOKEN":csrf}, onConnectedHandler, onErrorHandler);
}

function disconnect(event) {
    stompClient.send(sendTo, {}, JSON.stringify({messageContent:"leave", type:"MSG_LEAVE"}));
    stompClient.unsubscribe(subscribeTo, {});

    stompClient.disconnect(onDisconnectedHandler, {});
}

function onConnectedHandler() {
    stompClient.subscribe(subscribeTo, onMessageReceivedHandler);
    // Send initial message with MSG_JOIN type so the server can display "X join the room"
    stompClient.send(sendTo, {}, JSON.stringify({messageContent:"join", type:"MSG_JOIN"}));
}

function onDisconnectedHandler() {
    document.location.replace("/");
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

    var messageElement = document.createElement("div");
    messageElement.classList.add("d-flex");

    if (messageReceived.type === "MSG_JOIN") {

        messageElement.classList.add("justify-content-center");

        var messageDiv = document.createElement("div");
        messageDiv.classList.add("d-flex");

        var messageParagraph = document.createElement("p");
        messageParagraph.classList.add("text-center");
        messageParagraph.classList.add("text-success");

        var usernameSpan = document.createElement("span");
        usernameSpan.classList.add("text-primary");
        usernameSpan.innerText = messageReceived.username;

        var infoText = document.createTextNode(" joined the room");

        messageParagraph.appendChild(usernameSpan);
        messageParagraph.appendChild(infoText);
        messageDiv.appendChild(messageParagraph);
        messageElement.appendChild(messageDiv);

    } else if (messageReceived.type === "MSG_LEAVE") {

        messageElement.classList.add("justify-content-center");

        var messageDiv = document.createElement("div");
        messageDiv.classList.add("d-flex");

        var messageParagraph = document.createElement("p");
        messageParagraph.classList.add("text-center");
        messageParagraph.classList.add("text-danger");

        var usernameSpan = document.createElement("span");
        usernameSpan.classList.add("text-primary");
        usernameSpan.innerText = messageReceived.username;

        var infoText = document.createTextNode(" left the room");

        messageParagraph.appendChild(usernameSpan);
        messageParagraph.appendChild(infoText);
        messageDiv.appendChild(messageParagraph);
        messageElement.appendChild(messageDiv);

    } else {
        // avatar
        var avatarDiv = document.createElement("div");
        avatarDiv.classList.add("p-2");

        var imgElement = document.createElement("img");
        imgElement.src = messageReceived.avatarUrl;
        imgElement.style.height = "50px";
        imgElement.style.width = "50px";

        avatarDiv.appendChild(imgElement);

        // username and time
        var usernameAndTimeDiv = document.createElement("div");
        usernameAndTimeDiv.classList.add("p-2");

        var timeSpan = document.createElement("span");
        timeSpan.classList.add("text-secondary");
        timeSpan.innerText = messageReceived.time;

        var usernameParagraph = document.createElement("p");
        usernameParagraph.classList.add("text-primary");
        usernameParagraph.innerText = messageReceived.username;

        usernameAndTimeDiv.appendChild(timeSpan);
        usernameAndTimeDiv.appendChild(usernameParagraph);

        // message content
        var messageDiv = document.createElement("div");
        messageDiv.classList.add("p-3");
        messageDiv.style.wordBreak = "break-word";

        var messageParagraph = document.createElement("p");
        messageParagraph.innerText = messageReceived.content;

        messageDiv.appendChild(messageParagraph);

        // now build all

        messageElement.appendChild(avatarDiv);
        messageElement.appendChild(usernameAndTimeDiv);
        messageElement.appendChild(messageDiv);
    }

    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

connect();
disconnectButton.addEventListener("click", disconnect, true);
messageForm.addEventListener("submit", sendMessage, true);