package dataaccess;

import model.UserData;
import java.util.HashMap;

public interface UserDataAccess {
    UserData addUser(String username, String password, String email);

    HashMap<String, UserData> listUsers() throws DataAccessException;
}
