package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import service.LogoutService;
import reqandres.*;
import spark.*;

public class Logout implements Route {

    private final AuthDataAccess authAccess;

    public Logout(AuthDataAccess authAccess){
        this.authAccess = authAccess;
    }

    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutService service = new LogoutService(request, authAccess);
        LogoutResult result = service.getResult();
        if (result.message() != null) {
            res.status(401);
        }
        else {
            res.status(200);
        }
        return new Gson().toJson(result);
    }
}