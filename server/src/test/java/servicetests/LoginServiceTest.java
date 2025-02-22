package servicetests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqAndRes.LoginRequest;
import service.LoginService;

class LoginServiceTest {

    private LoginService service;

    @BeforeEach
    void setUp() {
        LoginRequest req = new LoginRequest("bob", "bob");
        this.service = new LoginService(req);
    }

    @Test
    void getGoodAuth() {
        service.createTestUser("bob", "bob", "bob");
        int expectedStatus = 200;
        int actualStatus = service.getResult().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getUnauthorized() {
        service.createTestUser("bob", "wrong password", "bob");
        int expectedStatus = 401;
        int actualStatus = service.getResult().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getNoUser() {
        service.createTestUser("bob's hacker david", "bob", "bob");
        int expectedStatus = 500;
        int actualStatus = service.getResult().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}