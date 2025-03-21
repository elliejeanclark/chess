package ui;

import client.ServerFacade;
import reqandres.*;
import model.*;
import chess.*;
import static ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private String authToken  = null;
    private ChessGame.TeamColor teamColor;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private ChessBoard currBoard = null;

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
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "exit" -> exit();
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
            return """
                    create <NAME> - create a game
                    list - list all games
                    join <ID> [WHITE|BLACK] - join a game based on ID and pick a color
                    observe <id> - watch a game
                    logout - logout of the system
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else {
            return """
                    exit - exit watch/playing mode.
                    quit - quit the program.
                    """;
        }
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult result = server.login(params[0], params[1]);
            if (result.message() == null) {
                state = State.SIGNEDIN;
                username = params[0];
                authToken = result.authToken();
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
                authToken = result.authToken();
                return String.format("Welcome %s. Type help to see new commands", username);
            }
            else {
                return result.message();
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email> as arguments.");
    }

    public String create(String... params) throws ResponseException {
        try {
            assertSignedIn();
        }
        catch (ResponseException e) {
            throw e;
        }
        if (params.length == 1) {
            CreateGameResult result = server.create(authToken, params[0]);
            if (result.message() == null) {
                return String.format("Game %s has been created", params[0]);
            }
            else {
                return result.message();
            }
        }
        throw new ResponseException(400, "Expected: <NAME> as arguments.");
    }

    public String list() throws ResponseException {
        try {
            assertSignedIn();
        }
        catch (ResponseException e) {
            throw e;
        }
        ListGamesResult result = server.list(authToken);
        if (result.message() == null) {
            String listOfGames = "";
            ArrayList<GameData> games = result.games();
            int i = 1;
            for (GameData data : games) {
                listOfGames += String.format("%d. GameID: %d, White: %s, Black: %s \n", i, data.gameID(), data.whiteUsername(), data.blackUsername());
                i++;
            }
            if (listOfGames.equals("")){
                return "No active games. Try creating a game!";
            }
            else {
                return listOfGames;
            }
        }
        else {
            return result.message();
        }
    }

    public String join(String... params) throws ResponseException {
        int givenGameID;
        try {
            assertSignedIn();
        }
        catch (ResponseException e) {
            throw e;
        }
        if (params.length != 2) {
            return "Incorrect amount of arguments";
        }
        else if (params[1] == "BLACK" || params[1] == "WHITE") {
            return "please enter a valid color to join";
        }
        else {
            String listresult = list();
            if (listresult.equals("No active games. Try creating a game!")) {
                return "There are no games, try creating a game before joining";
            }
            else {
                try {
                    givenGameID = Integer.parseInt(params[0]);
                } catch (NumberFormatException e) {
                    return "Please enter an actual number for gameID";
                }
                ChessGame.TeamColor color;
                if (params[1].equals("white")) {
                    color = ChessGame.TeamColor.WHITE;
                }
                else {
                    color = ChessGame.TeamColor.BLACK;
                }
                JoinGameResult result = server.join(authToken, givenGameID, color);
                if (result.message() == null) {
                    joinGetBoardSetState(givenGameID, color);
                    return stringifiedBoard(currBoard);
                }
                else {
                    return result.message();
                }
            }
        }
    }

    private void joinGetBoardSetState(int givenGameID, ChessGame.TeamColor color) throws ResponseException {
        ListGamesResult gamesResult = server.list(authToken);
        ArrayList<GameData> games = gamesResult.games();
        for (GameData data : games) {
            int gameID = data.gameID();
            if (gameID == givenGameID) {
                currBoard = data.game().getBoard();
            }
        }
        state = State.PLAYINGGAME;
        teamColor = color;
    }

    public String logout() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "Error: you are already logged out");
        }
        else {
            LogoutResult result = server.logout(authToken);
            if (result.message() == null) {
                username = null;
                authToken = null;
                state = State.SIGNEDOUT;
                return String.format("You have been logged out.");
            }
            else {
                return result.message();
            }
        }
    }

    public String observe(String... params) throws ResponseException {
        int givenGameID;
        try {
            assertSignedIn();
        } catch (ResponseException e) {
            throw e;
        }
        if (params.length != 1) {
            return "Invalid arguments. Enter a valid gameID.";
        }
        else {
            String listresult = list();
            if (listresult.equals("No active games. Try creating a game!")) {
                return "There are no games, try creating a game before watching.";
            }
            else {
                try {
                    givenGameID = Integer.parseInt(params[0]);
                } catch (NumberFormatException e) {
                    return "Please enter an actual number for gameID";
                }
                ListGamesResult gamesResult = server.list(authToken);
                ArrayList<GameData> games = gamesResult.games();
                for (GameData data : games) {
                    int gameID = data.gameID();
                    if (gameID == givenGameID) {
                        currBoard = data.game().getBoard();
                    }
                }
                state = State.WATCHINGGAME;
                teamColor = ChessGame.TeamColor.WHITE;
                return stringifiedBoard(currBoard);
            }
        }
    }

    private String exit() {
        if (state == State.SIGNEDOUT || state == State.SIGNEDIN) {
            return "You aren't watching or playing a game.";
        }
        else {
            state = State.SIGNEDIN;
            teamColor = null;
            return "No longer playing/watching game.";
        }
    }

    public String stringifiedBoard(ChessBoard board) {
        boolean whiteView = false;
        String result = "";
        if (teamColor == ChessGame.TeamColor.WHITE) {
            whiteView = true;
        }

        if (whiteView) {
            for (int i = 9; i >= 0; i--) {
                if (i == 0 || i == 9) {
                    result += RESET_TEXT_COLOR;
                    result += SET_BG_COLOR_DARK_GREEN;
                    result += "    a  b  c  d  e  f  g  h    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        else {
            for (int i = 0; i <= 9; i++) {
                if (i == 0 || i == 9) {
                    result += RESET_TEXT_COLOR;
                    result += SET_BG_COLOR_DARK_GREEN;
                    result += "    h  g  f  e  d  c  b  a    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        return result;
    }

    private String drawRow(ChessBoard board, int i) {
        String row = "";
        row += SET_BG_COLOR_DARK_GREEN;
        row += " ";
        row += i;
        row += " ";
        if (i % 2 == 0) {
            for (int j = 1; j <= 8; j++) {
                if (j % 2 != 0) {
                    row += SET_BG_COLOR_WHITE;
                    row += " ";
                    row += getPieceColor(board, i, j);
                    row += getPieceType(board, i, j);
                    row += RESET_TEXT_COLOR;
                    row += " ";
                }
                else {
                    row += SET_BG_COLOR_BLACK;
                    row += " ";
                    row += getPieceColor(board, i, j);
                    row += getPieceType(board, i, j);
                    row += RESET_TEXT_COLOR;
                    row += " ";
                }
            }
        }
        else {
            for (int j = 1; j <= 8; j++) {
                if (j % 2 != 0) {
                    row += SET_BG_COLOR_BLACK;
                    row += " ";
                    row += getPieceColor(board, i, j);
                    row += getPieceType(board, i, j);
                    row += RESET_TEXT_COLOR;
                    row += " ";
                }
                else {
                    row += SET_BG_COLOR_WHITE;
                    row += " ";
                    row += getPieceColor(board, i, j);
                    row += getPieceType(board, i, j);
                    row += RESET_TEXT_COLOR;
                    row += " ";
                }
            }
        }
        row += SET_BG_COLOR_DARK_GREEN;
        row += " ";
        row += i;
        row += " ";
        return row;
    }

    private String getPieceType(ChessBoard board, int i, int j) {
        ChessPosition position = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return " ";
        }
        else {
            ChessPiece.PieceType pieceType = piece.getPieceType();
            return switch (pieceType) {
                case ChessPiece.PieceType.KING -> "K";
                case ChessPiece.PieceType.QUEEN -> "Q";
                case ChessPiece.PieceType.PAWN -> "P";
                case ChessPiece.PieceType.ROOK -> "R";
                case ChessPiece.PieceType.KNIGHT -> "N";
                case ChessPiece.PieceType.BISHOP -> "B";
            };
        }
    }

    private String getPieceColor(ChessBoard board, int i, int j) {
        ChessPosition position = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return "";
        }
        else {
            ChessGame.TeamColor color = piece.getTeamColor();
            return switch (color) {
                case ChessGame.TeamColor.WHITE -> SET_TEXT_COLOR_RED;
                case ChessGame.TeamColor.BLACK -> SET_TEXT_COLOR_BLUE;
            };
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
