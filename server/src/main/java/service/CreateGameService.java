package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import reqandres.CreateGameRequest;
import reqandres.CreateGameResult;

public class CreateGameService {

    private final CreateGameRequest req;
    private CreateGameResult res;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;

    public CreateGameService(CreateGameRequest req, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public CreateGameResult createGame() {
        return new CreateGameResult(401, 12);
    }

}
