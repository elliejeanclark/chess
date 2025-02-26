package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.AuthDataAccess;
import reqandres.*;
import service.LoginService;
import spark.*;

public class Login implements Route {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;

    public Login(UserDataAccess userAccess, AuthDataAccess authAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }

    public Object handle(Request req, Response res) {
        String body = req.body();
        LoginRequest request = new Gson().fromJson(body, LoginRequest.class);
        LoginService service = new LoginService(request, authAccess, userAccess);
        LoginResult result = service.login();
        return new Gson().toJson(result);
    }
}