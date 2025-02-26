package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import reqandres.ListGamesRequest;
import reqandres.ListGamesResult;

public class ListGamesService {

    private final ListGamesRequest req;
    private ListGamesResult res;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;

    public ListGamesService(ListGamesRequest req, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public ListGamesResult listGames() {
        return new ListGamesResult(500, null);
    }

}
