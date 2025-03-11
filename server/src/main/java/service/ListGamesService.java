package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import reqandres.ListGamesRequest;
import reqandres.ListGamesResult;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;

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

    private boolean checkAuthorized() {
        boolean authorized = false;
        try {
            AuthData authData = authAccess.getAuth(req.authToken());
            if (authData != null) {
                authorized = true;
            }
        }
        catch (DataAccessException e) {
            authorized = false;
        }
        return authorized;
    }

    public ListGamesResult listGames() {
        boolean authorized = checkAuthorized();
        if (authorized) {
            HashMap<Integer, GameData> games = gameAccess.getGames();
            ArrayList<GameData> arrayOfGames = new ArrayList<>();
            arrayOfGames.addAll(games.values());
            this.res = new ListGamesResult(arrayOfGames, null);
        }
        else {
            this.res = new ListGamesResult(null, "Error: unauthorized");
        }
        return res;
    }
}
