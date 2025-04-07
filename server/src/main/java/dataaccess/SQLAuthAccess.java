package dataaccess;

import chess.ChessGame;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthAccess implements AuthDataAccess {

    public SQLAuthAccess() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var gs = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        gs.setString(i + 1, p);
                    }
                    else if (param == null)  {
                        gs.setNull(i + 1, NULL);
                    }
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

    public void createAuth(String authToken, String username) throws DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            executeUpdate(statement, authToken, username);
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    public String getUsername(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE authToken=?";
            try (var as = conn.prepareStatement(statement)) {
                as.setString(1, authToken);
                try (var rs = as.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return "Error reading username";
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var as = conn.prepareStatement(statement)) {
                as.setString(1, authToken);
                try (var rs = as.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public HashMap<String, AuthData> getVerUsers() throws DataAccessException {
        var result = new HashMap<String, AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth";
            try (var as = conn.prepareStatement(statement)) {
                try (var rs = as.executeQuery()) {
                    while (rs.next()) {
                        result.put(readAuth(rs).authToken(), readAuth(rs));
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return result;
    }

    public void clear() {
        try {
            var statement = "TRUNCATE auth";
            executeUpdate(statement);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
