package dataaccess;

import chess.ChessGame;
import model.*;
import java.util.HashMap;

public class MemoryGameAccess implements GameDataAccess {
    private final HashMap<Integer, GameData> games;
    private int gameID;

    public MemoryGameAccess() {
        this.games = new HashMap<>();
        this.gameID = 0;
    }

    public int getNextID() {
        int nextID = gameID;
        gameID++;
        return nextID;
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.put(gameID, gameData);
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public HashMap<Integer, GameData> getGames(){
        return games;
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
