package dataaccess;

import model.UserData;

import java.util.HashMap;

public interface UserDataAccess {
    void createUser(UserData user);

    void removeUser(String username) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    boolean checkUsernameTaken(String username);

    boolean verifyUser(String username, String password) throws DataAccessException;

    HashMap<String, UserData> getUsers();

    void clear();
}
