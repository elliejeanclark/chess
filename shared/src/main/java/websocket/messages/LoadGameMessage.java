package websocket.messages;

import chess.*;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {
    private String game;

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        ChessGame.TeamColor color;
        if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
            color = ChessGame.TeamColor.WHITE;
        }
        else {
            color = ChessGame.TeamColor.BLACK;
        }
        this.game = new StringyBoard(game.getBoard(), color).getBoard();
    }

    public void setGame(String message) {
        this.game = message;
    }

    public String getGame() {
        return game;
    }

    public String toString() {
        return new Gson().toJson(game);
    }
}
