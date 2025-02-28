package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import reqandres.JoinGameRequest;
import reqandres.JoinGameResult;

public class JoinGameService {

    private final JoinGameRequest req;
    private JoinGameResult res;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;

    public JoinGameService(JoinGameRequest req, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public JoinGameResult joinGame() {
        return new JoinGameResult(400);
    }
}
