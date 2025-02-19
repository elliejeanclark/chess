package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryUserAccess implements UserDataAccess {

    private final HashMap<String, UserData> users;

    public MemoryUserAccess() {
        users = new HashMap<>();
    }

    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        users.put(username, user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }
}
