package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.*;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameAccess implements GameDataAccess {

    public SQLGameAccess() throws DataAccessException {
        configureDatabase();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var gs = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) gs.setString(i + 1, p);
                    else if (param instanceof Integer p) gs.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) gs.setString(i + 1, serializeChessGame(p));
                    else if (param == null) gs.setNull(i + 1, NULL);
                }
                gs.executeUpdate();

                var rs = gs.getGeneratedKeys();
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

    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        try {
            var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            return executeUpdate(statement, whiteUsername, blackUsername, gameName, game);
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
            try (var gs = conn.prepareStatement(statement)) {
                gs.setInt(1, gameID);
                try (var rs = gs.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException (String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = deserializeChessGame(rs.getString("game"));
        return new GameData(id, whiteUsername, blackUsername, gameName, game);
    }

    public HashMap<Integer, GameData> getGames() {
        var result = new HashMap<Integer, GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM pet";
            try (var gs = conn.prepareStatement(statement)){
                try (var rs = gs.executeQuery()) {
                    while (rs.next()) {
                        result.put(readGame(rs).gameID(), readGame(rs));
                    }
                }
            }
        }
        catch (Exception e) {
            return result;
        }
        return result;
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException{
        try {
            var statement = "UPDATE games SET game=? WHERE gameID=?";
            executeUpdate(statement, game, gameID);
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void setPlayer(ChessGame.TeamColor playerColor, String username, int gameID) throws DataAccessException {
        try {
            if (playerColor == ChessGame.TeamColor.WHITE) {
                var statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
                executeUpdate(statement, username, gameID);
            }
            else {
                var statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
                executeUpdate(statement, username, gameID);
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() {
        var statement = "TRUNCATE games";
        try {
            executeUpdate(statement);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
