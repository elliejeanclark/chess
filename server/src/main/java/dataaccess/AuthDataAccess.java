package dataaccess;

import model.*;

import java.util.HashMap;

public interface AuthDataAccess {
    void createAuth(String authToken, String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    HashMap<String, AuthData> getVerUsers() throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    void clear();
}
