package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import com.google.gson.Gson;
import reqandres.ClearApplicationResult;
import service.ClearApplicationService;
import spark.*;

public class ClearApplication implements Route {

    private AuthDataAccess authAccess;
    private GameDataAccess gameAccess;
    private UserDataAccess userAccess;

    public ClearApplication(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public Object handle(Request req, Response res) {
        ClearApplicationService service = new ClearApplicationService(authAccess, userAccess, gameAccess);
        ClearApplicationResult result = service.clear();
        return new Gson().toJson(result);
    }
}
