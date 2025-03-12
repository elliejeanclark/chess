package dataaccess;

import chess.ChessGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.GameData;

import java.util.HashMap;

class MemoryGameAccessTest {

    private MemoryGameAccess games;

    @BeforeEach
    void setUp() {
        this.games = new MemoryGameAccess();
    }

    @Test
    void createGame() {
        games.createGame("white", "black", "test game", new ChessGame());
        int actualSize = games.getGames().size();
        int expectedSize = 1;
        Assertions.assertEquals(actualSize, expectedSize);
    }

    @Test
    void getGame() {
        ChessGame testGame = new ChessGame();
        games.createGame("white", "black", "test game", testGame);
        GameData expectedData = new GameData(1, "white", "black", "test game", testGame);
        GameData actualData = games.getGame(1);
        Assertions.assertEquals(expectedData, actualData);
    }

    @Test
    void getGames() {
        ChessGame testGame1 = new ChessGame();
        ChessGame testGame2 = new ChessGame();
        games.createGame("white", "black", "test game1", testGame1);
        games.createGame("white", "black", "test game2", testGame2);
        HashMap<Integer, GameData> expectedGames = new HashMap<>();
        GameData expectedData1 = new GameData(1, "white", "black", "test game1", testGame1);
        GameData expectedData2 = new GameData(2, "white", "black", "test game2", testGame2);
        expectedGames.put(1, expectedData1);
        expectedGames.put(2, expectedData2);
        Assertions.assertEquals(expectedGames, games.getGames());
    }

    @Test
    void updateGame() {
        ChessGame testGame = new ChessGame();
        ChessGame testUpdatedGame = new ChessGame();
        games.createGame( "white", "black", "test game", testGame);
        games.updateGame(1, testUpdatedGame);

        HashMap<Integer, GameData> expectedGames = new HashMap<>();
        GameData expectedData = new GameData(1, "white", "black", "test game", testUpdatedGame);
        expectedGames.put(1, expectedData);
        Assertions.assertEquals(expectedGames.get(1), games.getGame(1));
    }
}