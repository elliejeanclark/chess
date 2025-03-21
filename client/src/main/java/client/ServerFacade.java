package client;

import chess.ChessGame;
import ui.ResponseException;

import com.google.gson.Gson;
import reqandres.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        RegisterRequest request = new RegisterRequest(username, password, email);
        var response = this.makeRequest("POST", path, request, null, RegisterResult.class);
        return response;
    }

    public LoginResult login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginRequest request = new LoginRequest(username, password);
        var response = this.makeRequest("POST", path, request, null, LoginResult.class);
        return response;
    }

    public CreateGameResult create(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        var response = this.makeRequest("POST", path, request, authToken, CreateGameResult.class);
        return response;
    }

    public LogoutResult logout(String authToken) throws ResponseException {
        var path = "/session";
        LogoutRequest request = new LogoutRequest(authToken);
        var response = this.makeRequest("DELETE", path, request, authToken, LogoutResult.class);
        return response;
    }

    public ListGamesResult list(String authToken) throws ResponseException {
        var path = "/game";
        ListGamesRequest request = new ListGamesRequest(authToken);
        var response = this.makeRequest("GET", path, request, authToken, ListGamesResult.class);
        return response;
    }

    public JoinGameResult join(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        var path = "/game";
        JoinGameRequest request = new JoinGameRequest(authToken, color, gameID);
        var response = this.makeRequest("PUT", path, request, authToken, JoinGameResult.class);
        return response;
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            if (method.equals("GET")) {
                http.connect();
                throwIfNotSuccessful(http);
                return readBody(http, responseClass);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (ResponseException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr!= null) {
                    var map = new Gson().fromJson(new InputStreamReader(respErr), HashMap.class);
                    String message = map.get("message").toString();
                    throw new ResponseException(status, message);
                }
            }

            throw new ResponseException(status, "other Failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
