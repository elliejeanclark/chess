package reqandres;

import model.AuthData;

public record RegisterResult(AuthData authData, int statusCode) {
}
