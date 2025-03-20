package reqandres;

public record LoginResult(String authToken, String username, String message) {}