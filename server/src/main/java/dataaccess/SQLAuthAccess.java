package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.HashMap;

public class SQLAuthAccess implements AuthDataAccess {

    public SQLAuthAccess() throws DataAccessException {
        configureDatabase();
    }

    public void createAuth(String authToken, String username) {

    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public HashMap<String, AuthData> getVerUsers() {
        return null;
    }

    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authID` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authID`),
              FOREIGN KEY username REFERENCES users(username)
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
