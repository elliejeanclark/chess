package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryAuthAccess implements AuthDataAccess {

    private final HashMap<String, AuthData> verifiedUsers;

    public MemoryAuthAccess() {
        verifiedUsers = new HashMap<>();
    }

    public HashMap<String, AuthData> getVerUsers() {
        return verifiedUsers;
    }

    public void createAuth(String authToken, String username) {
        AuthData authData = new AuthData(authToken, username);
        verifiedUsers.put(authToken, authData);
    }

    public AuthData getAuth(String authToken) {
        return verifiedUsers.get(authToken);
    }

    public void clear() {
        verifiedUsers.clear();
    }

    public String getUsername(String authToken) throws DataAccessException {
        AuthData data = verifiedUsers.get(authToken);
        if (data == null) {
            throw new DataAccessException("user doesn't exist");
        }
        return data.username();
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            if (verifiedUsers.containsKey(authToken)) {
                verifiedUsers.remove(authToken);
            }
            else {
                throw new DataAccessException("Unauthorized");
            }
        }
        catch (DataAccessException e) {
            throw e;
        }
    }
}
