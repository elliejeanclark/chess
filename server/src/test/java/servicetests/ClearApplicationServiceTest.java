package servicetests;

import chess.ChessGame;
import dataaccess.MemoryAuthAccess;
import dataaccess.MemoryGameAccess;
import dataaccess.MemoryUserAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import service.ClearApplicationService;

class ClearApplicationServiceTest {

    private MemoryAuthAccess authAccess;
    private MemoryUserAccess userAccess;
    private MemoryGameAccess gameAccess;
    private ClearApplicationService service;

    @BeforeEach
    public void setUp() {
        this.authAccess = new MemoryAuthAccess();
        this.gameAccess = new MemoryGameAccess();
        this.userAccess = new MemoryUserAccess();
        this.service = new ClearApplicationService(authAccess, userAccess, gameAccess);
        UserData user1 = new UserData("bob", "bob", "bob");
        UserData user2 = new UserData("gerald", "gerald", "gerald");
        authAccess.createAuth("bob's auth token", "bob");
        authAccess.createAuth("gerald's auth token", "gerald");
        gameAccess.createGame(1, "white", "black", "game1", new ChessGame());
        gameAccess.createGame(2, "white", "black", "game2", new ChessGame());
        userAccess.createUser(user1);
        userAccess.createUser(user2);
    }

    @Test
    public void clearApplication() {
        int expectedStatus = 200;
        int actualStatus = service.clear().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}