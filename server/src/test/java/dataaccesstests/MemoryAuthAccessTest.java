package dataaccesstests;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.AuthData;

class MemoryAuthAccessTest {

    private MemoryAuthAccess verUsers;

    @BeforeEach
    void setup() {
        this.verUsers = new MemoryAuthAccess();
    }

    @Test
    void createAuth() {
        AuthData testAuthData = new AuthData("a test auth token", "test username");
        verUsers.createAuth(testAuthData);
        AuthData returnedAuth = verUsers.getAuth("a test auth token");
        Assertions.assertEquals(testAuthData, returnedAuth);
    }

    @Test
    void removeAuth() {
        AuthData testAuthData = new AuthData("a test auth token", "test username");
        verUsers.createAuth(testAuthData);
        try {
            verUsers.removeAuth("a test auth token");
        }
        catch (DataAccessException ignored) {}
        int expectedSize = 0;
        Assertions.assertEquals(expectedSize, verUsers.getVerUsers().size());
    }
}