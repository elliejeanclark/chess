package server.serverwebsocket;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import chess.*;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public WebSocketHandler(UserDataAccess userAccess, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> join(command.getAuthToken(), command.getGameID(), session);
            case UserGameCommand.CommandType.LEAVE -> exit(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void join(String authToken, int gameID, Session session) throws IOException {
        try {
            String username = authAccess.getUsername(authToken);
            GameData gameData = gameAccess.getGame(gameID);
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            if (username.equals(whiteUsername)) {
                connections.add(username, session);
                var message = String.format("%s has joined the game as the white player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
            }
            else if (username.equals(blackUsername)) {
                connections.add(username, session);
                var message = String.format("%s has joined the game as the black player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
            } else {
                connections.add(username, session);
                var message = String.format("%s is watching the game", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
            }
        }
        catch (DataAccessException ex) {
            var message = ex.getMessage();
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.broadcast(null, notification);
        }
    }

    private void exit(String authToken, int gameID, Session session) throws IOException {
        try {
            String username = authAccess.getUsername(authToken);
            GameData gameData = gameAccess.getGame(gameID);
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            if (username.equals(whiteUsername)) {
                var message = String.format("%s has left the game as the white player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
                connections.remove(username);
            }
            else if (username.equals(blackUsername)) {
                var message = String.format("%s has left the game as the black player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
                connections.remove(username);
            }
            else {
                var message = String.format("%s is no longer watching the game", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification);
                connections.remove(username);
            }
        }
        catch (DataAccessException ex) {
            var message = ex.getMessage();
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.broadcast(null, notification);
        }
    }
}
