package servicetests;

import dataaccess.MemoryAuthAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqandres.LogoutRequest;
import service.LogoutService;

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
        int expectedStatus = 200;
        int actualStatus = service.getResult().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getBadLogout() {
        service.createTestAuth("bad auth token");
        int expectedStatus = 401;
        int actualStatus = service.getResult().statusCode();
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}