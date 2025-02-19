package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import reqAndRes.*;
import model.*;

import java.time.DateTimeException;

public class UserService {
    private final LoginRequest req;
    private LoginResult res;
    private final MemoryUserAccess userAccess;

    public UserService(LoginRequest req) {
        this.req = req;
        userAccess = new MemoryUserAccess();
    }

    public UserData getUser() {
        return userAccess.getUser(req.username());
    }

    public AuthData getAuth() throws DataAccessException {
        UserData user = getUser();
        if (user != null) {
            String username = req.username();
            String authToken = "an auth token";
            return new AuthData(authToken, username);
        }
        else {
            throw new DataAccessException("user does not exist");
        }
    }
}
