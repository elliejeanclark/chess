package servicetests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqandres.LoginRequest;
import service.LoginService;
import dataaccess.*;

class LoginServiceTest {

    private LoginService service;
    private AuthDataAccess authAccess;
    private UserDataAccess userAccess;

    @BeforeEach
    void setUp() {
        LoginRequest req = new LoginRequest("bob", "bob");
        this.authAccess = new MemoryAuthAccess();
        this.userAccess = new MemoryUserAccess();
        this.service = new LoginService(req, authAccess, userAccess);
    }

    @Test
    void getGoodAuth() {
        service.createTestUser("bob", "bob", "bob");
        int expectedStatus = 200;
        int actualStatus = service.login().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getUnauthorized() {
        service.createTestUser("bob", "wrong password", "bob");
        int expectedStatus = 401;
        int actualStatus = service.login().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getNoUser() {
        service.createTestUser("bob's hacker david", "bob", "bob");
        int expectedStatus = 500;
        int actualStatus = service.login().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}