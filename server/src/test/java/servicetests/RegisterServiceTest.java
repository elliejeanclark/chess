package servicetests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;
import reqandres.RegisterRequest;

class RegisterServiceTest {

    private RegisterService service;

    @BeforeEach
    void setUp() {
        RegisterRequest req = new RegisterRequest("bob", "bob", "bob");
        this.service = new RegisterService(req);
    }

    @Test
    void registerAvailableUsername() {
        service.createTestUser("notBob35", "bob", "bob");
        int expectedStatus = 200;
        int actualStatus = service.register().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void registerTakenUsername() {
        service.createTestUser("bob", "bob", "bob");
        int expectedStatus = 403;
        int actualStatus = service.register().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}