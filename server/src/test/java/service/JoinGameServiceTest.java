package service;

import chess.ChessGame;
import dataaccess.MemoryAuthAccess;
import dataaccess.MemoryGameAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reqandres.JoinGameRequest;

class JoinGameServiceTest {
    private MemoryGameAccess gameAccess;
    private MemoryAuthAccess authAccess;
    private ChessGame testGame;

    @BeforeEach
    public void setUp() {
        this.gameAccess = new MemoryGameAccess();
        this.authAccess = new MemoryAuthAccess();
        this.testGame = new ChessGame();
        gameAccess.getNextID();
        gameAccess.createGame(1, "white", null, "first game", testGame);
        authAccess.createAuth("bob's auth token", "bob");
    }

    @Test
    public void joinGameSuccess() {
        JoinGameRequest req = new JoinGameRequest("bob's auth token", ChessGame.TeamColor.BLACK, 1);
        JoinGameService service = new JoinGameService(req, authAccess, gameAccess);
        Assertions.assertNull(null, service.joinGame().message());
    }

    @Test
    public void joinGameUnauthorized() {
        JoinGameRequest req = new JoinGameRequest("not bob's auth token", ChessGame.TeamColor.BLACK, 1);
        JoinGameService service = new JoinGameService(req, authAccess, gameAccess);
        Assertions.assertEquals("Error: unauthorized", service.joinGame().message());
    }

    @Test
    public void joinGameAlreadyTaken() {
        gameAccess.getNextID();
        gameAccess.createGame(2, "white", "another person", "first game", testGame);
        JoinGameRequest req = new JoinGameRequest("bob's auth token", ChessGame.TeamColor.BLACK, 2);
        JoinGameService service = new JoinGameService(req, authAccess, gameAccess);
        Assertions.assertEquals("Error: already Taken", service.joinGame().message());
    }
}