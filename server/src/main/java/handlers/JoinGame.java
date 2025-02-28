package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import com.google.gson.Gson;
import reqandres.JoinGameRequest;
import reqandres.JoinGameResult;
import service.JoinGameService;
import spark.*;

public class JoinGame implements Route{

    private AuthDataAccess authAccess;
    private GameDataAccess gameAccess;

    public JoinGame(AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    public Object handle(Request req, Response res) {
        String body = req.body();
        String authToken = req.headers("authorization");
        String bodyPlusAuthToken = body + authToken;
        JoinGameRequest request = new Gson().fromJson(bodyPlusAuthToken, JoinGameRequest.class);
        JoinGameService service = new JoinGameService(request, authAccess, gameAccess);
        JoinGameResult result = service.joinGame();
        return new Gson().toJson(result);
    }
}
