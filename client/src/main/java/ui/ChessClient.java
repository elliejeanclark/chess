package ui;

import client.ServerFacade;
import reqandres.*;

import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public State getState() {
        return state;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> signIn(params);
                case "help" -> help();
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> "That is an invalid command. Please try again.\n" + help();
            };
        }
        catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    login <USERNAME> <PASSWORD> - to login and play chess
                    register <USERNAME> <PASSWORD> <EMAIL> - to create a new account
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else if (state == State.SIGNEDIN) {
            return "here are some commands for once you have logged in";
        }
        else {
            return "here are some commands for after you have joined a game";
        }
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult result = server.login(params[0], params[1]);
            if (result.message() == null) {
                state = State.SIGNEDIN;
                username = params[0];
                return String.format("You signed in as %s. Type help to see new commands.", username);
            }
            else {
                return result.message();
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> as arguments. Please try again.");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult result = server.register(params[0], params[1], params[2]);
            if (result.message() == null) {
                state = State.SIGNEDIN;
                username = params[0];
                return String.format("Welcome %s. Type help to see new commands", username);
            }
            else {
                return result.message();
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email> as arguments.");
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
