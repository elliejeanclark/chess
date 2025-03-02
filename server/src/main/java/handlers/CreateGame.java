package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import com.google.gson.Gson;
import reqandres.CreateGameRequest;
import reqandres.CreateGameResult;
import reqandres.GameName;
import service.CreateGameService;
import spark.*;

public class CreateGame implements Route {

    private AuthDataAccess authAccess;
    private GameDataAccess gameAccess;

    public CreateGame(AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public Object handle(Request req, Response res) {
        String body = req.body();
        String authToken = req.headers("authorization");
        GameName gameName = new Gson().fromJson(body, GameName.class);
        CreateGameRequest request = new CreateGameRequest(authToken, gameName.gameName());
        CreateGameService service = new CreateGameService(request, authAccess, gameAccess);
        CreateGameResult result = service.createGame();
        if (result.gameID() == null) {
            if (result.message() == "Error: unauthorized") {
                res.status(401);
            }
            else {
                res.status(400);
            }
        }
        return new Gson().toJson(result);
    }
}
