package dataaccesstests;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameAccess;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class SQLGameAccessTest {

    private SQLGameAccess gameAccess;

    @BeforeEach
    void setup() {
        try {
            gameAccess = new SQLGameAccess();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        gameAccess.clear();
    }

    @Test
    void createAndGetGoodGame() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        GameData expectedGame = new GameData(1, "white", "black", "name", testGame);
        GameData actualGame = gameAccess.getGame(1);
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    void createAndGetBadGame() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        Assertions.assertNull(gameAccess.getGame(2));
    }

    @Test
    void testGetGamesPopulated() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        gameAccess.createGame("white", "black", "name", testGame);
        HashMap<Integer, GameData> givenGames = gameAccess.getGames();
        int givenSize = givenGames.size();
        Assertions.assertEquals(2, givenSize);
    }

    @Test
    void testGetGamesNone() throws DataAccessException {
        Assertions.assertEquals(0, gameAccess.getGames().size());
    }

    @Test
    void testUpdateGoodGame() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        ChessGame testGame2 = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        gameAccess.updateGame(1, testGame2);
        GameData expectedGame =  new GameData(1, "white", "black", "name", testGame2);
        Assertions.assertEquals(expectedGame, gameAccess.getGame(1));
    }

    @Test
    void testUpdateBadGame() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        gameAccess.updateGame(3, new ChessGame());
        GameData expectedGame =  new GameData(1, "white", "black", "name", testGame);
        Assertions.assertEquals(expectedGame, gameAccess.getGame(1));
    }

    @Test
    void testSetGoodPlayer() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", null, "name", testGame);
        gameAccess.setPlayer(ChessGame.TeamColor.BLACK, "black", 1);
        GameData expectedGame = new GameData(1, "white", "black", "name", testGame);
        Assertions.assertEquals(expectedGame, gameAccess.getGame(1));
    }

    @Test
    void testSetBadPlayer() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        gameAccess.setPlayer(ChessGame.TeamColor.BLACK, "new", 3);
        GameData expectedGame =  new GameData(1, "white", "black", "name", testGame);
        Assertions.assertEquals(expectedGame, gameAccess.getGame(1));
    }

    @Test
    void testClear() throws DataAccessException {
        ChessGame testGame = new ChessGame();
        gameAccess.createGame("white", "black", "name", testGame);
        gameAccess.clear();
        Assertions.assertEquals(0, gameAccess.getGames().size());
    }
}