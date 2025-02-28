package servicetests;

import chess.ChessGame;
import dataaccess.MemoryAuthAccess;
import dataaccess.MemoryGameAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reqandres.CreateGameRequest;
import service.CreateGameService;

class CreateGameServiceTest {
    private MemoryGameAccess gameAccess;
    private MemoryAuthAccess authAccess;
    private CreateGameService service;
    private ChessGame testGame;

    @BeforeEach
    void setup() {
        this.gameAccess = new MemoryGameAccess();
        this.authAccess = new MemoryAuthAccess();
        this.testGame = new ChessGame();
        gameAccess.getNextID();
        gameAccess.createGame(1, "white", "black", "first game", testGame);
        authAccess.createAuth("bob's auth token", "bob");
    }

    @Test
    void createGameSuccess() {
        CreateGameRequest req = new CreateGameRequest("bob's auth token", "second game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        int expectedStatus = 200;
        int actualStatus = service.createGame().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void createGameUnauthorized() {
        CreateGameRequest req = new CreateGameRequest("not Bob's auth token", "second game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        int expectedStatus = 401;
        int actualStatus = service.createGame().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void createGameAlreadyTaken() {
        CreateGameRequest req = new CreateGameRequest("bob's auth token", "first game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        int expectedStatus = 400;
        int actualStatus = service.createGame().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}