package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqandres.LoginRequest;
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
        Assertions.assertEquals("bob", service.login().username());
    }

    @Test
    void getUnauthorized() {
        service.createTestUser("bob", "wrong password", "bob");
        Assertions.assertEquals(null, service.login().username());
    }

    @Test
    void getNoUser() {
        service.createTestUser("bob's hacker david", "bob", "bob");
        Assertions.assertEquals(null, service.login().username());
    }
}