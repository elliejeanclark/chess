package dataaccess;

import model.*;
import java.util.HashMap;

public class MemoryAuthAccess {

    private final HashMap<String, AuthData> verifiedUsers;

    public MemoryAuthAccess() {
        verifiedUsers = new HashMap<>();
    }

    public HashMap<String, AuthData> getVerUsers() {
        return verifiedUsers;
    }

    public void createAuth(AuthData data) {
        String authToken = data.authToken();
        verifiedUsers.put(authToken, data);
    }

    public AuthData getAuth(String authToken) {
        return verifiedUsers.get(authToken);
    }

    public void removeAuth(String authToken) throws DataAccessException {
        try {
            if (verifiedUsers.containsKey(authToken)) {
                verifiedUsers.remove(authToken);
            }
            else {
                throw new DataAccessException("Unauthorized Log Out");
            }
        }
        catch (DataAccessException e) {
            throw e;
        }
    }
}
