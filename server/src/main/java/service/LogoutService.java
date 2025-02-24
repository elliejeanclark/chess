package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthAccess;
import model.UserData;
import model.AuthData;
import reqandres.*;

public class LogoutService {
    private final LogoutRequest req;
    private LogoutResult res;
    private final MemoryAuthAccess authAccess;

    public LogoutService(LogoutRequest req) {
        this.req = req;
        this.authAccess = new MemoryAuthAccess();
    }

    public void createTestAuth(String authToken) {
        AuthData testAuth = new AuthData(authToken, "bob");
        authAccess.createAuth(testAuth);
    }

    private void removeAuthorization() throws DataAccessException {
        try {
            authAccess.removeAuth(req.authToken());
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
