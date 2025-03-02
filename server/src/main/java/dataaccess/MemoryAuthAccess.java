package dataaccess;

import model.*;

import javax.xml.crypto.Data;
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

    public AuthData getAuthByUsername(String username) throws DataAccessException {
        for (AuthData authData : verifiedUsers.values()) {
            if (authData.username().equals(username)) {
                return authData;
            }
        }
        throw new DataAccessException("no auth data exists for that user");
    }

    public void clear() {
        verifiedUsers.clear();
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
