package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import com.google.gson.Gson;
import reqandres.JoinGameRequest;
import reqandres.JoinGameResult;
import reqandres.ColorID;
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
        ColorID colorID = new Gson().fromJson(body, ColorID.class);
        JoinGameRequest request = new JoinGameRequest(authToken, colorID.playerColor(), colorID.gameID());
        if (colorID.gameID() == null) {
            res.status(400);
            return new Gson().toJson( new JoinGameResult("Error: bad request"));
        }
        JoinGameService service = new JoinGameService(request, authAccess, gameAccess);
        JoinGameResult result = service.joinGame();
        if (result.message() == "Error: already Taken") {
            res.status(403);
        }
        else if (result.message() == "Error: unauthorized") {
            res.status(401);
        }
        else if (result.message() == "Error: bad request") {
            res.status(400);
        }
        else {
            res.status(200);
        }
        return new Gson().toJson(result);
    }
}
