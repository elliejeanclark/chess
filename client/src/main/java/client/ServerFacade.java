package client;

import ui.ResponseException;

import com.google.gson.Gson;
import reqandres.*;
import java.net.*;
import java.io.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        RegisterRequest request = new RegisterRequest(username, password, email);
        var response = this.makeRequest("POST", path, request, RegisterResult.class);
        return response;
    }

    public LoginResult login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginRequest request = new LoginRequest(username, password);
        var response = this.makeRequest("POST", path, request, LoginResult.class);
        return response;
    }

    public CreateGameResult create(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        var response = this.makeRequest("POST", path, request, CreateGameResult.class);
        return response;
    }

    public LogoutResult logout(String authToken) throws ResponseException {
        var path = "/session";
        LogoutRequest request = new LogoutRequest(authToken);
        var response = this.makeRequest("DELETE", path, request, LogoutResult.class);
        return response;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
                    throw ResponseException.fromJson(respErr);
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
