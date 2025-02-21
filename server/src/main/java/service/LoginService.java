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
            throw e;
        }
    }

    public LoginResult getAuth() throws DataAccessException {
        UserData user = getUser();
        try {
            if (user != null) {
                String username = req.username();
                String authToken = "an auth token";
                AuthData auth = new AuthData(authToken, username);
                this.res = new LoginResult(auth);
                return res;
            }
            else {
                throw new DataAccessException("user does not exist");
            }
        }
        catch (DataAccessException exception) {
            throw(exception);
        }
    }
}
