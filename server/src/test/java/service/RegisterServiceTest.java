package service;

import dataaccess.MemoryAuthAccess;
import dataaccess.MemoryUserAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqandres.RegisterRequest;

class RegisterServiceTest {

    private RegisterService service;

    @BeforeEach
    void setUp() {
        RegisterRequest req = new RegisterRequest("bob", "bob", "bob");
        MemoryAuthAccess authAccess = new MemoryAuthAccess();
        MemoryUserAccess userAccess = new MemoryUserAccess();
        this.service = new RegisterService(req, authAccess, userAccess);
    }

    @Test
    void registerAvailableUsername() {
        service.createTestUser("notBob35", "bob", "bob");
        Assertions.assertEquals("bob", service.register().username());
    }

    @Test
    void registerTakenUsername() {
        service.createTestUser("bob", "bob", "bob");
        Assertions.assertNull(null, service.register().username());
    }
}