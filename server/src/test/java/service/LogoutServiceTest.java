package service;

import dataaccess.MemoryAuthAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqandres.LogoutRequest;

class LogoutServiceTest {

    private LogoutService service;

    @BeforeEach
    void setUp() {
        LogoutRequest outReq = new LogoutRequest("correct auth token");
        MemoryAuthAccess authAccess = new MemoryAuthAccess();
        this.service = new LogoutService(outReq, authAccess);
    }

    @Test
    void getGoodLogout() {
        service.createTestAuth("correct auth token");
        Assertions.assertNull(null, service.getResult().message());
    }

    @Test
    void getBadLogout() {
        service.createTestAuth("bad auth token");
        Assertions.assertEquals("Error: Unauthorized", service.getResult().message());
    }
}