package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import dataaccess.MemoryAuthAccess;
import reqandres.*;
import model.*;

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

    public RegisterResult register() {
        return new RegisterResult(new AuthData("null", "null"), 500);
    }
}
