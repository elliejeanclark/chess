package dataaccesstests;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameAccess;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLGameAccessTest {

    private SQLGameAccess gameAccess;

    @BeforeEach
    void setup() {
        try {
            gameAccess = new SQLGameAccess();
            gameAccess.clear();
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
    }
}