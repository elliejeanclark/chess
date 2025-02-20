package dataaccess;

import model.*;
import dataaccess.DataAccessException;

import java.util.HashMap;

public class MemoryUserAccess implements UserDataAccess {

    private final HashMap<String, UserData> users;

    public HashMap<String, UserData> getUsers() {
        return users;
    }

    public MemoryUserAccess() {
        users = new HashMap<>();
    }

    public void createUser(UserData user) {
        String username = user.username();
        users.put(username, user);
    }

    public void removeUser(String username) throws DataAccessException {
        try {
            UserData removed = users.remove(username);
            if (removed == null) {
                throw new DataAccessException("No User with that Username Exists.");
            }
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        try {
            if (user != null) {
                return user;
            }
            else {
                throw new DataAccessException("User does not exist");
            }
        }
        catch (DataAccessException e){
            throw e;
        }
    }
}
