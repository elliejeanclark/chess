package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import dataaccess.MemoryAuthAccess;
import reqandres.*;
import model.*;

import java.util.UUID;

public class RegisterService {

    private final RegisterRequest req;
    private RegisterResult res;
    private final MemoryUserAccess userAccess;
    private final MemoryAuthAccess authAccess;

    public RegisterService(RegisterRequest req) {
        this.req = req;
        this.userAccess = new MemoryUserAccess();
        this.authAccess = new MemoryAuthAccess();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register() {
        try {
            boolean usernameTaken = userAccess.checkUsernameTaken(req.username());
            if (usernameTaken) {
                throw new DataAccessException("That username is taken");
            }
            else {
                UserData userData = new UserData(req.username(), req.password(), req.email());
                userAccess.createUser(userData);
                String authToken = generateToken();
                AuthData authData = new AuthData(authToken, req.username());
                authAccess.createAuth(authData);
                this.res = new RegisterResult(authData, 200);
            }
        } catch (DataAccessException e) {
            this.res = new RegisterResult(null, 403);
        }
        return res;
    }
}
