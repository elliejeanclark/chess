package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLAuthAccessTest {

    private SQLAuthAccess authAccess;

    @BeforeEach
    void setup() {
        try {
            authAccess = new SQLAuthAccess();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        authAccess.clear();
    }

    @Test
    void testCreateGoodAuth() throws DataAccessException {
        authAccess.createAuth("authToken", "username");
        AuthData expectedAuth = new AuthData("authToken", "username");
        Assertions.assertEquals(expectedAuth, authAccess.getAuth("authToken"));
    }

    @Test
    void testGetBadAuth() throws DataAccessException {
        Assertions.assertNull(authAccess.getAuth("authToken"));
    }

    @Test
    void deleteExistingAuth() throws DataAccessException {
        authAccess.createAuth("authToken", "username");
        authAccess.deleteAuth("authToken");
        Assertions.assertNull(authAccess.getAuth("authToken"));
    }

    @Test
    void deleteNotExistingAuth() throws DataAccessException {
        authAccess.createAuth("authToken", "username");
        authAccess.deleteAuth("incorrectAuthToken");
        Assertions.assertNotNull(authAccess.getAuth("authToken"));
    }

    @Test
    void getListValidUsers() throws DataAccessException {
        authAccess.createAuth("authToken", "username");
        authAccess.createAuth("authToken2", "username2");
        Assertions.assertEquals(2, authAccess.getVerUsers().size());
    }

    @Test
    void getEmptyListValidUsers() throws DataAccessException {
        Assertions.assertEquals(0, authAccess.getVerUsers().size());
    }

    @Test
    void testClear() throws DataAccessException {
        authAccess.createAuth("authToken", "username");
        authAccess.createAuth("authToken2", "username2");
        authAccess.clear();
        Assertions.assertEquals(0, authAccess.getVerUsers().size());
    }
}