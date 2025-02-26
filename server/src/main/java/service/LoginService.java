package service;

import dataaccess.*;
import reqandres.*;
import model.*;
import java.util.UUID;

public class LoginService {
    private final LoginRequest req;
    private LoginResult res;
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;

    public LoginService(LoginRequest req, AuthDataAccess authAccess, UserDataAccess userAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.userAccess = userAccess;
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

    public LoginResult login() {
        try {
            UserData user = getUser();
            if (!user.password().equals(req.password())) {
                throw new DataAccessException("wrong password");
            }
            else {
                String username = req.username();
                String authToken = generateToken();
                AuthData auth = new AuthData(authToken, username);
                authAccess.createAuth(authToken, username);
                this.res = new LoginResult(auth, 200);
                return res;
            }
        }
        catch (DataAccessException exception) {
            if (exception.getMessage() == "User does not exist") {
                this.res = new LoginResult(null, 500);
            }
            else {
                this.res = new LoginResult(null, 401);
            }
            return res;
        }
    }
}
