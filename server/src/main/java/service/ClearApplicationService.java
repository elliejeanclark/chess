package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import reqandres.ClearApplicationResult;
import chess.ChessGame;
import reqandres.JoinGameRequest;

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
        return new ClearApplicationResult(200);
    }
}
