package handlers;

import com.google.gson.Gson;
import service.LogoutService;
import reqandres.*;
import spark.*;

public class Logout implements Route {
    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        LogoutRequest request = new Gson().fromJson(authToken, LogoutRequest.class);
        LogoutService service = new LogoutService(request);
        LogoutResult result = service.getResult();
        return new Gson().toJson(result);
    }
}