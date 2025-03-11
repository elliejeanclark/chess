package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class SQLGameAccess implements GameDataAccess {
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    }

    public GameData getGame(int gameID) {
        return null;
    }

    public HashMap<Integer, GameData> getGames() {
        return null;
    }

    public void updateGame(int gameID, ChessGame game) {

    }

    public int getNextID() {
        return 0;
    }

    public void setPlayer(ChessGame.TeamColor playerColor, String username, int gameID) {

    }

    public void clear() {

    }
}
