package dataaccess;

import model.UserData;

import java.util.HashMap;

public interface UserDataAccess {
    void createUser(UserData user);

    void removeUser(String username) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    boolean checkUsernameTaken(String username);

    HashMap<String, UserData> getUsers();

    void clear();
}
