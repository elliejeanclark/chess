package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import com.google.gson.Gson;
import reqandres.ListGamesRequest;
import reqandres.ListGamesResult;
import service.ListGamesService;
import spark.*;

public class ListGames implements Route {

    private AuthDataAccess authAccess;
    private GameDataAccess gameAccess;

    public ListGames(AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesService service = new ListGamesService(request, authAccess, gameAccess);
        ListGamesResult result = service.listGames();
        if (result.message() != null) {
            res.status(401);
        }
        else {
            res.status(200);
        }
        return new Gson().toJson(result);
    }
}
