package server;

import dataaccess.*;
import server.serverwebsocket.WebSocketHandler;
import spark.*;
import handlers.*;

public class Server {

    private WebSocketHandler webSocketHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        webSocketHandler = new WebSocketHandler();
        // Using SQL Access.
        try {
            UserDataAccess userAccess = new SQLUserAccess();
            AuthDataAccess authAccess = new SQLAuthAccess();
            GameDataAccess gameAccess = new SQLGameAccess();
            createRoutes(userAccess, authAccess, gameAccess);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes(UserDataAccess userAccess, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        Spark.webSocket("/ws", new WebSocketHandler());
        Spark.post("/session", new Login(userAccess, authAccess));
        Spark.delete("/session", new Logout(authAccess));
        Spark.post("/user", new Register(userAccess, authAccess));
        Spark.get("/game", new ListGames(authAccess, gameAccess));
        Spark.post("/game", new CreateGame(authAccess, gameAccess));
        Spark.put("/game", new JoinGame(authAccess, gameAccess));
        Spark.delete("/db", new ClearApplication(authAccess, userAccess, gameAccess));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
