package handlers;

import reqandres.RegisterRequest;
import reqandres.RegisterResult;
import service.RegisterService;
import com.google.gson.Gson;
import spark.*;

public class Register implements Route {
    public Object handle(Request req, Response res) {
        String body = req.body();
        RegisterRequest request = new Gson().fromJson(body, RegisterRequest.class);
        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();
        return new Gson().toJson(result);
    }
}
