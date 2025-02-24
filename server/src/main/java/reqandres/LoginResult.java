package reqandres;

import model.*;

public record LoginResult(AuthData authData, int statusCode) {}