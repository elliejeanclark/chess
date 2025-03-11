package service;

import chess.ChessGame;
import dataaccess.MemoryAuthAccess;
import dataaccess.MemoryGameAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reqandres.CreateGameRequest;

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
        gameAccess.createGame( "white", "black", "first game", testGame);
        authAccess.createAuth("bob's auth token", "bob");
    }

    @Test
    void createGameSuccess() {
        CreateGameRequest req = new CreateGameRequest("bob's auth token", "second game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        Assertions.assertEquals(2, service.createGame().gameID());
    }

    @Test
    void createGameUnauthorized() {
        CreateGameRequest req = new CreateGameRequest("not Bob's auth token", "second game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        Assertions.assertEquals(null, service.createGame().gameID());
    }

    @Test
    void createGameAlreadyTaken() {
        CreateGameRequest req = new CreateGameRequest("bob's auth token", "first game");
        this.service = new CreateGameService(req, authAccess, gameAccess);
        Assertions.assertEquals(null, service.createGame().gameID());
    }
}