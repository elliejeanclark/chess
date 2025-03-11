package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserAccess implements UserDataAccess {

    public SQLUserAccess() throws DataAccessException {
        configureDatabase();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var us = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) us.setString(i + 1, p);
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

    public boolean verifyUser(String username, String password) throws DataAccessException {
        try {
            var storedHashedPassword = getUser(username).password();
            return BCrypt.checkpw(password, storedHashedPassword);
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData user) {
        String username = user.username();
        String hashedPassword = hashPassword(user.password());
        String email = user.email();

        var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try {
            executeUpdate(statement, username, email, hashedPassword);
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    private String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public void removeUser(String username) throws DataAccessException {
        var statement = "DELETE FROM users WHERE username=?";
        executeUpdate(statement, username);
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, email, password FROM users WHERE username=?";
            try (var us = conn.prepareStatement(statement)) {
                us.setString(1, username);
                try (var rs = us.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var email = rs.getString("email");
        var password = rs.getString("password");
        return new UserData(username, password, email);
    }

    public boolean checkUsernameTaken(String username) {
        try {
            UserData user = getUser(username);
            if (user != null) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (DataAccessException e) {
            return true;
        }
    }

    public HashMap<String, UserData> getUsers() {
        var result = new HashMap<String, UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users";
            try (var us = conn.prepareStatement(statement)) {
                try (var rs = us.executeQuery()) {
                    while (rs.next()) {
                        result.put(readUser(rs).username(), readUser(rs));
                    }
                }
            }
        }
        catch (Exception e) {
            return result;
        }
        return result;
    }

    public void clear() {
        var statement = "TRUNCATE users";
        try {
            executeUpdate(statement);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
