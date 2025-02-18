package handlers;

import spark.*;

public class Login implements Route {
    public Object handle(Request req, Response res) {
        return "I'm logging in";
    }
}
