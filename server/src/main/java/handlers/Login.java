package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import reqAndRes.*;
import service.LoginService;
import spark.*;

public class Login implements Route {
    public Object handle(Request req, Response res) {
        String body = req.body();
        LoginRequest request = new Gson().fromJson(body, LoginRequest.class);
        LoginService service = new LoginService(request);
        try {
            LoginResult result = service.getAuth();
            return new Gson().toJson(result);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}