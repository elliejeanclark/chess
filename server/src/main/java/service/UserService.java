package service;

import reqAndRes.*;

public class UserService {
    public LoginResult login(LoginRequest loginRequest) {
        return new LoginResult(loginRequest);
    }
}
