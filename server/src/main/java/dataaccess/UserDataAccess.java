package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void createUser(UserData user);

    void removeUser(String username) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
}
