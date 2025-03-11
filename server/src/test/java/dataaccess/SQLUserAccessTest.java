package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class SQLUserAccessTest {

    private SQLUserAccess userAccess;

    @BeforeEach
    void setup() {
        try {
            userAccess = new SQLUserAccess();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        userAccess.clear();
    }

    @Test
    void createGoodUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        UserData returnedUser = userAccess.getUser(testUser.username());
        Assertions.assertEquals(testUser.username(), returnedUser.username());
    }

    @Test
    void createBadUser() {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        Assertions.assertThrows(RuntimeException.class, () -> {userAccess.createUser(testUser);});
    }

    @Test
    void removeGoodUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        userAccess.removeUser(testUser.username());
        int expectedSize = 0;
        Assertions.assertEquals(expectedSize, userAccess.getUsers().size());
    }

    @Test
    void removeBadUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        userAccess.removeUser("incorrectUsername");
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, userAccess.getUsers().size());
    }

    @Test
    void verifyGoodUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        boolean verified = userAccess.verifyUser("user", "password");
        Assertions.assertTrue(verified);
    }

    @Test
    void verifyBadUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        boolean verified = userAccess.verifyUser("user", "wrongPassword");
        Assertions.assertFalse(verified);
    }
}