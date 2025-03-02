package reqandres;

import model.AuthData;

public record RegisterResult(String username, String authToken, String message) {
}
