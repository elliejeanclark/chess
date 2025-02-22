package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthAccess;
import reqAndRes.*;
import model.*;

public class LogoutService {
    private final LogoutRequest req;
    private LoginResult res;
    private final MemoryAuthAccess authAccess;

    public LogoutService(LogoutRequest req) {
        this.req = req;
        this.authAccess = new MemoryAuthAccess();
    }

    private void removeAuthorization() throws DataAccessException {
        try {
            authAccess.removeAuth(req.authToken());
        }
        catch (DataAccessException e) {
            throw e;
        }
    }
}
