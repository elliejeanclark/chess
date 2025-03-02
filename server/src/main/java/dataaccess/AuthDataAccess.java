package dataaccess;

import model.*;

import java.util.HashMap;

public interface AuthDataAccess {
    void createAuth(String authToken, String username);

    AuthData getAuth(String authToken);

    AuthData getAuthByUsername(String username) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    HashMap<String, AuthData> getVerUsers();

    void clear();
}
