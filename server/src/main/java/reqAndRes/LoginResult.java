package reqAndRes;

import model.*;

public record LoginResult(AuthData authData, int statusCode) {}