package dataaccess;

import chess.*;
import model.*;

import java.util.HashMap;

public interface GameDataAccess {
    int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    HashMap<Integer, GameData> getGames();

    void updateGame(int gameID, ChessGame game) throws DataAccessException;

    void setPlayer(ChessGame.TeamColor playerColor, String username, int gameID);

    void clear();
}
