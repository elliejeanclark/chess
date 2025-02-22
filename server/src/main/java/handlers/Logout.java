package handlers;

import com.google.gson.Gson;
import reqAndRes.*;
import service.LogoutService;
import spark.*;

public class Logout implements Route {
    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        LogoutRequest request = new Gson().fromJson(authToken, LogoutRequest.class);
//        LogoutService service = new LogoutService(request);
//        LogoutResult result = service.removeAuth();
//        return new Gson().toJson(result);
        return "logging out";
    }
}