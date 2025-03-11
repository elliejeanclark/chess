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
        authAccess.createAuth(generateToken(), username);
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
            if (!userAccess.verifyUser(user.username(), req.password())) {
                throw new DataAccessException("Error: wrong password");
            }
            else {
                String username = req.username();
                String authToken = generateToken();
                authAccess.createAuth(authToken, username);
                this.res = new LoginResult(authToken, username, null);
                return res;
            }
        }
        catch (DataAccessException exception) {
            if (exception.getMessage() == "User does not exist") {
                this.res = new LoginResult(null, null, exception.getMessage());
            }
            else if (exception.getMessage() == "no auth data exists for that user") {
                this.res = new LoginResult(null, null, exception.getMessage());
            }
            else {
                this.res = new LoginResult(null, null, exception.getMessage());
            }
            return res;
        }
    }
}
