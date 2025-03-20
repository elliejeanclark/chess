package service;

import reqandres.*;
import dataaccess.*;

public class LogoutService {
    private final LogoutRequest req;
    private LogoutResult res;
    private final AuthDataAccess authAccess;

    public LogoutService(LogoutRequest req, AuthDataAccess authAccess) {
        this.req = req;
        this.authAccess = authAccess;
    }

    public void createTestAuth(String authToken) {
        try {
            authAccess.createAuth(authToken, "bob");
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            int origSize = authAccess.getVerUsers().size();
            removeAuthorization();
            int newSize = authAccess.getVerUsers().size();
            if (origSize == newSize || origSize == 0) {
                this.res = new LogoutResult("Error: Unauthorized");
            }
            else {
                this.res = new LogoutResult(null);
            }
        } catch (DataAccessException e) {
            this.res = new LogoutResult("Error: Unauthorized");
        }
        return res;
    }
}
