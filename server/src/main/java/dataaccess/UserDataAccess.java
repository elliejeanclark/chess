package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void createUser(String username, String password, String email);

    void removeUser(String username);

    UserData getUser(String username);
}
