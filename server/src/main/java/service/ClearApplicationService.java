package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import reqandres.ClearApplicationResult;

public class ClearApplicationService {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public ClearApplicationService(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public ClearApplicationResult clear() {
        authAccess.clear();
        userAccess.clear();
        gameAccess.clear();
        return new ClearApplicationResult(200);
    }
}
