package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import model.*;
import reqandres.ClearApplicationResult;
import chess.ChessGame;
import reqandres.JoinGameRequest;

import java.util.HashMap;

public class ClearApplicationService {

    private ClearApplicationResult res;
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public ClearApplicationService(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public ClearApplicationResult clear() {
        try {
            HashMap<Integer, GameData> games = gameAccess.getGames();
            for (GameData game : games.values()) {
                gameAccess.removeGame(game.gameID());
            }

            HashMap<String, AuthData> verUsers = authAccess.getVerUsers();
            for (AuthData user : verUsers.values()) {
                authAccess.deleteAuth(user.authToken());
            }

            HashMap<String, UserData> users = userAccess.getUsers();
            for (UserData user : users.values()) {
                userAccess.removeUser(user.username());
            }

            return new ClearApplicationResult(200);
        } catch (DataAccessException e) {
            return new ClearApplicationResult(500);
        }
    }
}
