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
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

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
            case UserGameCommand.CommandType.LEAVE -> exit(command.getAuthToken(), command.getGameID());
            case UserGameCommand.CommandType.MAKE_MOVE -> {
                MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(), moveCommand.getMove());
            }
        }
    }

    private void join(String authToken, int gameID, Session session) throws IOException {
        try {
            String username = authAccess.getUsername(authToken);
            GameData gameData = gameAccess.getGame(gameID);
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            if (username.equals(whiteUsername)) {
                connections.add(username, session, gameID);
                var message = String.format("%s has joined the game as the white player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
            }
            else if (username.equals(blackUsername)) {
                connections.add(username, session, gameID);
                var message = String.format("%s has joined the game as the black player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
            } else {
                connections.add(username, session, gameID);
                var message = String.format("%s is watching the game", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
            }
        }
        catch (DataAccessException ex) {
            var message = ex.getMessage();
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.broadcast(null, notification, gameID);
        }
    }

    private void exit(String authToken, int gameID) throws IOException {
        try {
            String username = authAccess.getUsername(authToken);
            GameData gameData = gameAccess.getGame(gameID);
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            if (username.equals(whiteUsername)) {
                gameAccess.setPlayer(ChessGame.TeamColor.WHITE, null, gameID);
                var message = String.format("%s has left the game as the white player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
                connections.remove(username);
            }
            else if (username.equals(blackUsername)) {
                gameAccess.setPlayer(ChessGame.TeamColor.BLACK, null, gameID);
                var message = String.format("%s has left the game as the black player", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
                connections.remove(username);
            }
            else {
                var message = String.format("%s is no longer watching the game", username);
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(username, notification, gameID);
                connections.remove(username);
            }
        }
        catch (DataAccessException ex) {
            var message = ex.getMessage();
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.broadcast(null, notification, gameID);
        }
    }

    private void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        try {
            String username = authAccess.getUsername(authToken);
            String opponentUsername = null;
            GameData gameData = gameAccess.getGame(gameID);
            ChessGame.TeamColor color;
            if (username.equals(gameData.blackUsername())) {
                if (gameData.whiteUsername() != null) {
                    opponentUsername = gameData.whiteUsername();
                }
                color = ChessGame.TeamColor.BLACK;
            }
            else {
                if (gameData.blackUsername() != null) {
                    opponentUsername = gameData.blackUsername();
                }
                color = ChessGame.TeamColor.WHITE;
            }
            ChessGame game = gameData.game();
            game.makeMove(move);
            gameAccess.updateGame(gameID, game);
            String whitePerspective = new StringyBoard(game.getBoard(), ChessGame.TeamColor.WHITE).getBoard();
            String blackPerspective = new StringyBoard(game.getBoard(), ChessGame.TeamColor.BLACK).getBoard();
            var whiteNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, whitePerspective);
            var blackNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, blackPerspective);

            if (color == ChessGame.TeamColor.BLACK) {
                connections.broadcastToBlackPlayer(username, blackNotification);
                connections.broadcastWhitePerspective(username, whiteNotification, gameID);
            }
            else {
                if (opponentUsername == null) {
                    connections.broadcastWhitePerspective(null, whiteNotification, gameID);
                }
                else {
                    connections.broadcastWhitePerspective(opponentUsername, whiteNotification, gameID);
                    connections.broadcastToBlackPlayer(opponentUsername, blackNotification);
                }
            }

            int rowFrom = move.getStartPosition().getRow();
            int colFrom = move.getStartPosition().getColumn();
            int rowTo = move.getEndPosition().getRow();
            int colTo = move.getEndPosition().getColumn();
            String moveMessage = String.format("%s made a move from %d,%d to %d,%d.", username, rowFrom, colFrom, rowTo, colTo);
            NotificationMessage moveNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, moveMessage);
            connections.broadcast(username, moveNotification, gameID);

            if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                String checkMessage = "White is in check";
                NotificationMessage checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                connections.broadcast(null, checkNotification, gameID);
            }
            if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                String checkMessage = "Black is in check";
                NotificationMessage checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                connections.broadcast(null, checkNotification, gameID);
            }
            if (game.isInStalemate(ChessGame.TeamColor.BLACK) && game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                String stalemateMessage = "Game ends in stalemate.";
                NotificationMessage stalemateNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, stalemateMessage);
                connections.broadcast(null, stalemateNotification, gameID);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                String checkmateMessage = "White is in checkmate, Black wins!";
                NotificationMessage checkmateNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkmateMessage);
                connections.broadcast(null, checkmateNotification, gameID);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                String checkmateMessage = "Black is in checkmate, White wins!";
                NotificationMessage checkmateNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkmateMessage);
                connections.broadcast(null, checkmateNotification, gameID);
            }
        }
        catch (Exception ex) {
            var message = ex.getMessage();
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            connections.broadcast(null, notification, gameID);
        }
    }
}
