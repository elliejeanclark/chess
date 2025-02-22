package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import reqAndRes.*;
import model.*;
import java.util.UUID;

public class LoginService {
    private final LoginRequest req;
    private LoginResult res;
    private final MemoryUserAccess userAccess;

    public LoginService(LoginRequest req) {
        this.req = req;
        userAccess = new MemoryUserAccess();
    }

    public void createTestUser(String username, String password, String email) {
        UserData testUser = new UserData(username, password, email);
        userAccess.createUser(testUser);
    }

    private UserData getUser() throws DataAccessException{
        try {
            UserData user = userAccess.getUser(req.username());
            return user;
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LoginResult getAuth() {
        try {
            UserData user = getUser();
            if (!user.password().equals(req.password())) {
                throw new DataAccessException("wrong password");
            }
            else {
                String username = req.username();
                String authToken = generateToken();
                AuthData auth = new AuthData(authToken, username);
                this.res = new LoginResult(auth, 200);
                return res;
            }
        }
        catch (DataAccessException exception) {
            if (exception.getMessage() == "User does not exist") {
                AuthData badAuth = new AuthData(exception.getMessage(), "error");
                this.res = new LoginResult(badAuth, 500);
            }
            else {
                AuthData badAuth = new AuthData(exception.getMessage(), "error");
                this.res = new LoginResult(badAuth, 401);
            }
            return res;
        }
    }
}
