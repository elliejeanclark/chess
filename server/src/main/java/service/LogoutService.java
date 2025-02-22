package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import reqAndRes.*;
import model.*;

public class LogoutService {
    private final LogoutRequest req;
    private LoginResult res;
    private final MemoryUserAccess userAccess;

    public LogoutService(LogoutRequest req) {
        this.req = req;
        this.userAccess = new MemoryUserAccess();
    }

    public void createTestUser(String username, String password, String email) {
        UserData testUser = new UserData(username, password, email);
        userAccess.createUser(testUser);
    }

//    private void removeUser() throws DataAccessException {
//        try {
//            removeUser(req.authToken());
//        }
//        catch (DataAccessException e) {
//            throw e;
//        }
//    }
}
