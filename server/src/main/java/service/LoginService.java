package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import reqAndRes.*;
import model.*;

public class LoginService {
    private final LoginRequest req;
    private LoginResult res;
    private final MemoryUserAccess userAccess;

    public LoginService(LoginRequest req) {
        this.req = req;
        userAccess = new MemoryUserAccess();
    }

    public UserData getUser() throws DataAccessException{
        try {
            UserData user = userAccess.getUser(req.username());
            return user;
        }
        catch (DataAccessException e) {
            return new UserData("bad", "user", "data");
        }
    }

    public AuthData getAuth() throws DataAccessException {
        UserData user = getUser();
        try {
            if (user != null) {
                String username = req.username();
                String authToken = "an auth token";
                return new AuthData(authToken, username);
            }
            else {
                throw new DataAccessException("user does not exist");
            }
        }
        catch (DataAccessException exception) {
            return new AuthData("NoUser", "No User");
        }
    }
}
