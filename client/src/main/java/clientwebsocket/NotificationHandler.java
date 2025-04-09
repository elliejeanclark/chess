package clientwebsocket;

import websocket.messages.LoadGameMessage;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage message);

    void notifyError(ErrorMessage message);

    void notifyNotification(NotificationMessage message);

    void notifyLoadGame(LoadGameMessage message);
}
