package server;

import dataaccess.*;
import spark.*;
import handlers.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        MemoryUserAccess userAccess = new MemoryUserAccess();
        MemoryAuthAccess authAccess = new MemoryAuthAccess();
        MemoryGameAccess gameAccess = new MemoryGameAccess();
        createRoutes(userAccess, authAccess, gameAccess);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes(UserDataAccess userAccess, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        Spark.get("/login", new Login(userAccess, authAccess));
        Spark.get("/logout", new Logout(authAccess));
        Spark.get("/register", new Register(userAccess, authAccess));
        Spark.get("/list", new ListGames(authAccess, gameAccess));
        Spark.get("/create", new CreateGame(authAccess, gameAccess));
        Spark.get("/join", new JoinGame(authAccess, gameAccess));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
