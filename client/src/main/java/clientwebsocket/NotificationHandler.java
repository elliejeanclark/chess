package clientwebsocket;

import websocket.messages.LoadGameMessage;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notifyError(ErrorMessage message);

    void notifyNotification(NotificationMessage message);

    void notifyLoadGame(LoadGameMessage message);
}
