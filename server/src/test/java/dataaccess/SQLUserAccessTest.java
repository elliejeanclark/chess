package dataaccess;

import dataaccess.SQLUserAccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void createUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        UserData returnedUser = userAccess.getUser(testUser.username());
        Assertions.assertEquals(testUser.username(), returnedUser.username());
    }

    @Test
    void removeUser() throws DataAccessException {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        userAccess.removeUser(testUser.username());
        int expectedSize = 0;
        Assertions.assertEquals(expectedSize, userAccess.getUsers().size());
    }

    @Test
    void checkUsernameTaken() {
        UserData testUser = new UserData("user", "password", "bob");
        userAccess.createUser(testUser);
        Assertions.assertThrows(RuntimeException.class, () -> {userAccess.createUser(testUser);});
    }
}