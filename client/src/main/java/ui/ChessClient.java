package ui;

import client.ServerFacade;
import clientwebsocket.NotificationHandler;
import clientwebsocket.WebSocketFacade;
import reqandres.*;
import model.*;
import chess.*;
import static ui.EscapeSequences.*;
import exception.ResponseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private String authToken  = null;
    private ChessGame.TeamColor teamColor;
    private int currGameID;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private ChessBoard currBoard = null;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final String serverUrl;
    private boolean inSession = false;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
            server = new ServerFacade(serverUrl);
            this.serverUrl = serverUrl;
            this.notificationHandler = notificationHandler;
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
                case "leave" -> leave();
                case "redraw" -> redraw();
                case "move" -> move(params);
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
        else if (state == State.PLAYINGGAME) {
            return """
                    redraw - redraws the chess board to its current state.
                    leave - leave the game.
                    move <row>,<col> <row>,<col> - move the piece at the first index to the second index
                    resign - forfeit the game.
                    legal <row><col> - allows you to see legal moves for a piece at the given index.
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else {
            return """
                    exit - exit watch/playing mode.
                    quit - quit the program.
                    help - list possible commands.
                    """;
        }
    }

    private ChessMove parseChessMove(String[] params) throws ResponseException {
        if (params.length == 2) {
            String param1 = params[0];
            String param2 = params[1];
            String[] param1Items = param1.split(",");
            String [] param2Items = param2.split(",");
            int rowFrom;
            int colFrom;
            int rowTo;
            int colTo;
            if (param1Items.length != 2) {
                try {
                    rowFrom = Integer.parseInt(param1Items[0]);
                } catch (NumberFormatException e) {
                    throw new ResponseException(400, "Please enter a valid number for the row.");
                }


                switch (param1Items[1].toLowerCase()) {
                    case "a" -> colFrom = 1;
                    case "b" -> colFrom = 2;
                    case "c" -> colFrom = 3;
                    case "d" -> colFrom = 4;
                    case "e" -> colFrom = 5;
                    case "f" -> colFrom = 6;
                    case "g" -> colFrom = 7;
                    case "h" -> colFrom = 8;
                    default -> colFrom = 0;
                };

                if (colFrom == 0) {
                    throw new ResponseException(400, "Please enter a valid letter for the column");
                }
            }
            else {
                throw new ResponseException(400, "Invalid first coordinate, please try again.");
            }

            if (param2Items.length != 2) {
                try {
                    rowTo = Integer.parseInt(param2Items[0]);
                } catch (NumberFormatException e) {
                    throw new ResponseException(400, "Please enter a valid number for the row.");
                }
                switch (param2Items[1].toLowerCase()) {
                    case "a" -> colTo = 1;
                    case "b" -> colTo = 2;
                    case "c" -> colTo = 3;
                    case "d" -> colTo = 4;
                    case "e" -> colTo = 5;
                    case "f" -> colTo = 6;
                    case "g" -> colTo = 7;
                    case "h" -> colTo = 8;
                    default -> colTo = 0;
                };

                if (colTo == 0) {
                    throw new ResponseException(400, "Please enter a valid letter for the column");
                }
            }
            else {
                throw new ResponseException(400, "Invalid second coordinate, please try again.");
            }

            ChessPosition startPosition = new ChessPosition(rowFrom, colFrom);
            ChessPosition endPosition = new ChessPosition(rowTo, colTo);
            ChessPiece piece = currBoard.getPiece(startPosition);
            if (piece == null) {
                throw new ResponseException(400, "There isn't a piece there on the board");
            }
            else if (piece.getTeamColor() != teamColor) {
                throw new ResponseException(400, "That's not your piece");
            }
            else {
                return new ChessMove(startPosition, endPosition, piece.getPieceType());
            }
        }
        else {
            throw new ResponseException(400, "Invalid number of arguments");
        }
    }

    private void validateMove(ChessGame game, ChessMove move) throws InvalidMoveException {
        game.makeMove(move);
    }

    public String move(String... params) throws ResponseException {
        assertSignedIn();
        if (state != State.PLAYINGGAME) {
            return "You cannot make a move if you aren't playing a game.";
        }
        ChessMove move;
        try {
            move = parseChessMove(params);
            ListGamesResult result = server.list(authToken);
            ChessGame currGame = null;
            ArrayList<GameData> games = result.games();
            for (GameData data : games) {
                if (data.gameID() == currGameID) {
                    currGame = data.game();
                }
            }
            if (currGame == null) {
                return "Unable to find the game in the database.";
            }
            try {
                validateMove(currGame, move);
            } catch (InvalidMoveException e) {
                return e.getMessage();
            }

            ws.makeMove(authToken, currGameID, move);
        } catch (ResponseException e) {
            return e.getMessage();
        }
        return "Successfully made move";
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
                listOfGames += String.format("%d. White: %s, Black: %s, Name: %s\n", i, data.whiteUsername(), data.blackUsername(), data.gameName());
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
        boolean gameExists = false;
        try {
            assertSignedIn();
        }
        catch (ResponseException e) {
            throw e;
        }
        if (params.length != 2) {
            return "Incorrect amount of arguments";
        }
        else if (params[1].equals("white") || params[1].equals("black")){
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

                ListGamesResult listResult = server.list(authToken);
                ArrayList<GameData> games = listResult.games();
                for (GameData data : games) {
                    if (data.gameID() == givenGameID) {
                        gameExists = true;
                    }
                }

                if (!gameExists) {
                    return "That game doesn't exist. Please enter a valid game";
                }

                ChessGame.TeamColor color;
                currGameID = givenGameID;
                if (params[1].equals("white")) {
                    color = ChessGame.TeamColor.WHITE;
                }
                else {
                    color = ChessGame.TeamColor.BLACK;
                }
                JoinGameResult result = server.join(authToken, givenGameID, color);
                if (result.message() == null) {
                    joinGetBoardSetState(givenGameID, color);
                    if (!inSession) {
                        ws = new WebSocketFacade(serverUrl, notificationHandler);
                        ws.joinGame(authToken, currGameID);
                        inSession = true;
                    }
                    else {
                        ws.joinGame(authToken, currGameID);
                    }
                    return stringifiedBoard(currBoard);
                }
                else {
                    return result.message();
                }
            }
        }
        else {
            return "That is not a valid color choice. Please try again.";
        }
    }

    public String redraw() {
        return stringifiedBoard(currBoard);
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
        boolean gameExists = false;
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
                        gameExists = true;
                    }
                }

                if (!gameExists) {
                    return "That game doesn't exist, please try again";
                }

                state = State.WATCHINGGAME;
                currGameID = givenGameID;
                if (!inSession) {
                    ws = new WebSocketFacade(serverUrl, notificationHandler);
                    ws.joinGame(authToken, currGameID);
                    inSession = true;
                }
                else {
                    ws.joinGame(authToken, currGameID);
                }
                teamColor = ChessGame.TeamColor.WHITE;
                return stringifiedBoard(currBoard);
            }
        }
    }

    private String leave() throws ResponseException {
        if (state == State.SIGNEDOUT || state == State.SIGNEDIN) {
            return "You aren't watching or playing a game.";
        }
        else {
            state = State.SIGNEDIN;
            teamColor = null;
            ws.leaveGame(authToken, currGameID);
            inSession = false;
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
                    result += SET_BG_COLOR_MAGENTA;
                    result += "    a  b  c  d  e  f  g  h    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i, whiteView);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        else {
            for (int i = 0; i <= 9; i++) {
                if (i == 0 || i == 9) {
                    result += RESET_TEXT_COLOR;
                    result += SET_BG_COLOR_MAGENTA;
                    result += "    h  g  f  e  d  c  b  a    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i, whiteView);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        return result;
    }

    private String drawRow(ChessBoard board, int i, boolean whiteView) {
        String row = "";
        row += drawHeader(i);
        if (i % 2 == 0) {
            row += rowGuts(board, i, whiteView, false);
        }
        else {
            row += rowGuts(board, i, whiteView, true);
        }
        row += drawHeader(i);
        return row;
    }

    private String drawHeader(int i) {
        String row = "";
        row += SET_BG_COLOR_MAGENTA;
        row += " ";
        row += i;
        row += " ";
        return row;
    }

    private String rowGuts(ChessBoard board, int i, boolean whiteView, boolean oddRow) {
        String row = "";
        String oddCol;
        String evenCol;
        if (oddRow) {
            oddCol = SET_BG_COLOR_BLACK;
            evenCol = SET_BG_COLOR_WHITE;
        }
        else {
            oddCol = SET_BG_COLOR_WHITE;
            evenCol = SET_BG_COLOR_BLACK;
        }

        if (whiteView) {
            for (int j = 1; j <= 8; j++) {
                row += getSquare(board, i, j, oddCol, evenCol);
            }
        }
        else {
            for (int j = 8; j >= 1; j--) {
                row += getSquare(board, i, j, oddCol, evenCol);
            }
        }
        return row;
    }

    private String getSquare(ChessBoard board, int i, int j, String oddCol, String evenCol) {
        String row = "";
        if (j % 2 != 0) {
            row += oddCol;
            row += " ";
            row += getPieceColor(board, i, j);
            row += getPieceType(board, i, j);
            row += RESET_TEXT_COLOR;
            row += " ";
        }
        else {
            row += evenCol;
            row += " ";
            row += getPieceColor(board, i, j);
            row += getPieceType(board, i, j);
            row += RESET_TEXT_COLOR;
            row += " ";
        }
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
