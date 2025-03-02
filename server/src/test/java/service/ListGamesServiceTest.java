package service;

import chess.ChessGame;
import dataaccess.MemoryGameAccess;
import dataaccess.MemoryAuthAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reqandres.ListGamesRequest;
import reqandres.ListGamesResult;
import model.GameData;

import java.util.HashMap;
import java.util.ArrayList;

class ListGamesServiceTest {

    private MemoryGameAccess gameAccess;
    private MemoryAuthAccess authAccess;
    private ListGamesService service;
    private ChessGame testGame;

    @BeforeEach
    void setUp() {
        this.gameAccess = new MemoryGameAccess();
        this.authAccess = new MemoryAuthAccess();
        this.testGame = new ChessGame();
        gameAccess.createGame(1, "white", "black", "testgame", testGame);
        authAccess.createAuth("bob's Auth Token", "bob");
    }

    @Test
    void listGamesAuthorized() {
        ListGamesRequest req = new ListGamesRequest("bob's Auth Token");
        this.service = new ListGamesService(req, authAccess, gameAccess);
        ArrayList<GameData> listOfGames = new ArrayList<>();
        listOfGames.add(new GameData(1, "white", "black", "testgame", testGame));
        ListGamesResult expectedResult = new ListGamesResult(listOfGames, null);
        ListGamesResult actualResult = service.listGames();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void listGamesUnauthorized() {
        ListGamesRequest req = new ListGamesRequest("not bob's Auth Token");
        this.service = new ListGamesService(req, authAccess, gameAccess);
        Assertions.assertEquals(null, service.listGames().games());
    }
}