package dataaccesstests;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

class MemoryUserAccessTest {

    private MemoryUserAccess access;

    @BeforeEach
    void setup() {
        access = new MemoryUserAccess();
    }

    @Test
    void getUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        access.createUser(testUser);
        UserData returnedUser = access.getUser(testUser.username());
        Assertions.assertEquals(testUser, returnedUser);
    }

    @Test
    void createUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        access.createUser(testUser);
        UserData returnedUser = access.getUser(testUser.username());
        Assertions.assertEquals(testUser, returnedUser);
    }

    @Test
    void removeUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        access.createUser(testUser);
        access.removeUser(testUser.username());
        int expectedSize = 0;
        Assertions.assertEquals(expectedSize, access.getUsers().size());
    }

    @Test
    void getBadUser() {
        String username = "bob";
        Assertions.assertThrows(DataAccessException.class, () -> {access.getUser(username);});
    }

    @Test
    void removeBadUser() {
        Assertions.assertThrows(DataAccessException.class, () -> {access.removeUser("Bob");});
    }
}