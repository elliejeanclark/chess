package clientwebsocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommand.*;
import websocket.messages.*;
import chess.ChessMove;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String> () {
                @Override
                public void onMessage(String message) {
                    ErrorMessage errorNotification = new Gson().fromJson(message, ErrorMessage.class);
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    LoadGameMessage loadGameNotification = new Gson().fromJson(message, LoadGameMessage.class);

                    if (errorNotification.getMessage() != null) {
                        notificationHandler.notifyError(errorNotification);
                    }
                    else if (notification.getMessage() != null) {
                        notificationHandler.notifyNotification(notification);
                    }
                    else if (loadGameNotification.getGame() != null) {
                        notificationHandler.notifyLoadGame(loadGameNotification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinGame(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            var command = new MakeMoveCommand(CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resignGame(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
