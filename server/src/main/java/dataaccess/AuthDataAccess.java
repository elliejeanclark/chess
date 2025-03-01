package dataaccess;

import model.*;

import java.util.HashMap;

public interface AuthDataAccess {
    void createAuth(String authToken, String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken) throws DataAccessException;

    HashMap<String, AuthData> getVerUsers();
}
