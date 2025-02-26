package handlers;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import reqandres.RegisterRequest;
import reqandres.RegisterResult;
import service.RegisterService;
import com.google.gson.Gson;
import spark.*;

public class Register implements Route {

    private final UserDataAccess userAccess;
    private final AuthDataAccess authAccess;

    public Register(UserDataAccess userAccess, AuthDataAccess authAccess){
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public Object handle(Request req, Response res) {
        String body = req.body();
        RegisterRequest request = new Gson().fromJson(body, RegisterRequest.class);
        RegisterService service = new RegisterService(request, authAccess, userAccess);
        RegisterResult result = service.register();
        return new Gson().toJson(result);
    }
}
