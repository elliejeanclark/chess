package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameAccess implements GameDataAccess {

    public SQLGameAccess() throws DataAccessException {
        configureDatabase();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var us = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) us.setString(i + 1, p);
                    else if (param instanceof Integer p) us.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) us.setString(i + 1, serializeChessGame(p));
                    else if (param == null) us.setNull(i + 1, NULL);
                }
                us.executeUpdate();

                var rs = us.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private String serializeChessGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeChessGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        try {
            var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            executeUpdate(statement, whiteUsername, blackUsername, gameName, game);
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public HashMap<Integer, GameData> getGames() {
        return null;
    }

    public void updateGame(int gameID, ChessGame game) {

    }

    public int getNextID() {
        return 0;
    }

    public void setPlayer(ChessGame.TeamColor playerColor, String username, int gameID) {

    }

    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256),
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
