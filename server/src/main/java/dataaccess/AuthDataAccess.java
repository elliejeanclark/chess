package dataaccess;

import model.*;

public interface AuthDataAccess {
    void createAuth(String authToken, String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken) throws DataAccessException;
}
