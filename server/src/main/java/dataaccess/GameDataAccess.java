package dataaccess;

import chess.*;
import model.*;

import java.util.HashMap;

public interface GameDataAccess {
    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(int gameID);

    HashMap<Integer, GameData> getGames();

    void updateGame(int gameID, ChessGame game);
}
