package servicetests;

import chess.ChessGame;
import dataaccess.MemoryGameAccess;
import dataaccess.MemoryAuthAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reqandres.ListGamesRequest;
import reqandres.ListGamesResult;
import service.ListGamesService;
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
        HashMap<String, ArrayList<GameData>> expectedMap = new HashMap<>();
        ArrayList<GameData> listOfGames = new ArrayList<>();
        listOfGames.add(new GameData(1, "white", "black", "testgame", testGame));
        expectedMap.put("games", listOfGames);
        ListGamesResult expectedResult = new ListGamesResult(200, expectedMap);
        ListGamesResult actualResult = service.listGames();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void listGamesUnauthorized() {
        ListGamesRequest req = new ListGamesRequest("not bob's Auth Token");
        this.service = new ListGamesService(req, authAccess, gameAccess);
        int expectedStatus = 401;
        int actualStatus = service.listGames().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}