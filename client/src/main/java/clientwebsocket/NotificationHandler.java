package clientwebsocket;

import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(String message);
}
