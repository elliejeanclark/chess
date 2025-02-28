package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import com.google.gson.Gson;
import reqandres.CreateGameRequest;
import reqandres.CreateGameResult;
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
        String bodyPlusAuthToken = body + authToken;
        CreateGameRequest request = new Gson().fromJson(bodyPlusAuthToken, CreateGameRequest.class);
        CreateGameService service = new CreateGameService(request, authAccess, gameAccess);
        CreateGameResult result = service.createGame();
        return new Gson().toJson(result);
    }
}
