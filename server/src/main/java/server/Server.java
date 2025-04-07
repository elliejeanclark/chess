package server;

import dataaccess.*;
import server.serverwebsocket.WebSocketHandler;
import spark.*;
import handlers.*;

public class Server {

    private static WebSocketHandler webSocketHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Using SQL Access.
        try {
            UserDataAccess userAccess = new SQLUserAccess();
            AuthDataAccess authAccess = new SQLAuthAccess();
            GameDataAccess gameAccess = new SQLGameAccess();
            webSocketHandler = new WebSocketHandler(userAccess, authAccess, gameAccess);
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
        Spark.webSocket("/ws", webSocketHandler);
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
