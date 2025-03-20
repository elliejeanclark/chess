package service;

import dataaccess.*;
import model.*;
import reqandres.*;

import java.util.UUID;

public class RegisterService {

    private final RegisterRequest req;
    private RegisterResult res;
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;

    public RegisterService(RegisterRequest req, AuthDataAccess authAccess, UserDataAccess userAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }

    public void createTestUser(String username, String password, String email) {
        UserData testUser = new UserData(username, password, email);
        userAccess.createUser(testUser);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register() {
        try {
            boolean usernameTaken = userAccess.checkUsernameTaken(req.username());
            if (usernameTaken) {
                throw new DataAccessException("Error: That username is taken");
            }
            else if (req.username() == null || req.password() == null || req.email() == null) {
                throw new DataAccessException("Error: Bad Request");
            }
            else {
                UserData userData = new UserData(req.username(), req.password(), req.email());
                userAccess.createUser(userData);
                String authToken = generateToken();
                authAccess.createAuth(authToken, req.username());
                this.res = new RegisterResult(req.username(), authToken, null);
            }
        } catch (DataAccessException e) {
            this.res = new RegisterResult(null, null, e.getMessage());
        }
        return res;
    }
}
