package service;

import dataaccess.*;
import reqandres.*;

public class LogoutService {
    private final LogoutRequest req;
    private LogoutResult res;
    private final AuthDataAccess authAccess;

    public LogoutService(LogoutRequest req, AuthDataAccess authAccess) {
        this.req = req;
        this.authAccess = authAccess;
    }

    public void createTestAuth(String authToken) {
        authAccess.createAuth(authToken, "bob");
    }

    private void removeAuthorization() throws DataAccessException {
        try {
            authAccess.deleteAuth(req.authToken());
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

    public LogoutResult getResult() {
        try {
            removeAuthorization();
            this.res = new LogoutResult(200);
        } catch (DataAccessException e) {
            this.res = new LogoutResult(401);
        }
        return res;
    }
}
