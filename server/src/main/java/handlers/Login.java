package handlers;

import com.google.gson.Gson;
import reqandres.*;
import service.LoginService;
import spark.*;

public class Login implements Route {
    public Object handle(Request req, Response res) {
        String body = req.body();
        LoginRequest request = new Gson().fromJson(body, LoginRequest.class);
        LoginService service = new LoginService(request);
        LoginResult result = service.login();
        return new Gson().toJson(result);
    }
}