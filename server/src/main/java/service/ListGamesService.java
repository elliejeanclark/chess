package service;

import dataaccess.AuthDataAccess;
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
        AuthData authData = authAccess.getAuth(req.AuthToken());
        if (authData != null) {
            authorized = true;
        }
        return authorized;
    }

    public ListGamesResult listGames() {
        boolean authorized = checkAuthorized();
        if (authorized) {
            HashMap<String, ArrayList<GameData>> listOfGames = new HashMap<>();
            HashMap<Integer, GameData> games = gameAccess.getGames();
            ArrayList<GameData> arrayOfGames = new ArrayList<>();
            arrayOfGames.addAll(games.values());
            listOfGames.put("games", arrayOfGames);
            this.res = new ListGamesResult(200, listOfGames);
        }
        else {
            this.res = new ListGamesResult(401, null);
        }
        return res;
    }
}
