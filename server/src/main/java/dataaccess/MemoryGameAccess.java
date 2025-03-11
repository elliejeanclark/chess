package dataaccess;

import chess.ChessGame;
import model.*;
import java.util.HashMap;

public class MemoryGameAccess implements GameDataAccess {
    private final HashMap<Integer, GameData> games;
    private int gameID;

    public MemoryGameAccess() {
        this.games = new HashMap<>();
        this.gameID = 1;
    }

    public void setPlayer(ChessGame.TeamColor playerColor, String username, int gameID) {
        GameData oldData = games.get(gameID);
        if (playerColor == ChessGame.TeamColor.BLACK) {
            String whiteUsername = oldData.whiteUsername();
            String gameName = oldData.gameName();
            ChessGame game = oldData.game();
            GameData newData = new GameData(gameID, whiteUsername, username, gameName, game);
            games.put(gameID, newData);
        } else {
            String blackUsername = oldData.blackUsername();
            String gameName = oldData.gameName();
            ChessGame game = oldData.game();
            GameData newData = new GameData(gameID, username, blackUsername, gameName, game);
            games.put(gameID, newData);
        }
    }

    private int incrementID() {
        int oldID = gameID;
        gameID++;
        return oldID;
    }

    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.put(gameID, gameData);
        int usedID = incrementID();
        return usedID;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public HashMap<Integer, GameData> getGames(){
        return games;
    }

    public void clear() {
        games.clear();
    }

    public void updateGame(int gameID, ChessGame game) {
        GameData oldData = games.get(gameID);
        String whiteUsername = oldData.whiteUsername();
        String blackUsername = oldData.blackUsername();
        String gameName = oldData.gameName();
        GameData newData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.put(gameID, newData);
    }
}
