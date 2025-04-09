package ui;

import client.ServerFacade;
import clientwebsocket.*;
import reqandres.*;
import model.*;
import chess.*;
import exception.ResponseException;
import websocket.messages.StringyBoard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private String username = null;
    private String authToken  = null;
    private ChessGame.TeamColor teamColor;
    private int currGameID;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private ChessBoard currBoard = null;
    private ChessGame currGame = null;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final String serverUrl;
    private boolean inSession = false;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
            server = new ServerFacade(serverUrl);
            this.serverUrl = serverUrl;
            this.notificationHandler = notificationHandler;
    }

    public State getState() { return state; }

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
                case "resign" -> resign();
                case "legal" -> highlightLegalMoves(params);
                case "quit" -> "quit";
                default -> "That is an invalid command. Please try again.\n" + help();
            };
        }
        catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() { return new Help(state).getHelp(); }

    private int getNumFromLetter(String letter) {
        int col;
        switch (letter.toLowerCase()) {
            case "a" -> col = 1;
            case "b" -> col = 2;
            case "c" -> col = 3;
            case "d" -> col = 4;
            case "e" -> col = 5;
            case "f" -> col = 6;
            case "g" -> col = 7;
            case "h" -> col = 8;
            default -> col = 0;
        };
        return col;
    }

    public String highlightLegalMoves(String... params) throws ResponseException {
        assertSignedIn();
        if (state != State.PLAYINGGAME) {
            return "You can only highlight legal moves if you are playing.";
        }
        else if (params.length == 2) {
            int row;
            int col;
            try {
                row = Integer.parseInt(params[0]);
            }
            catch (NumberFormatException e) {
                throw new ResponseException(400, "Please enter a valid number for the row.");
            }
            col = getNumFromLetter(params[1]);
            if (row < 1 || row > 8) {
                throw new ResponseException(400, "Out of bounds row.");
            }
            if (col == 0) {
                throw new ResponseException(400, "Please enter a valid letter for the column");
            }
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = currBoard.getPiece(position);
            if (piece == null) {
                return "There is no piece there.";
            }
            else if (piece.getTeamColor() != teamColor) {
                return "That is not one of your pieces.";
            }
            else {
                return new StringyBoard(currGame, teamColor, position).getBoard();
            }
        }
        else {
            return "Invalid number of arguments.";
        }
    }

    private ChessMove parseChessMove(String[] params) throws ResponseException {
        if (params.length == 2 || params.length == 3) {
            String param1 = params[0];
            String param2 = params[1];
            String[] param1Items = param1.split(",");
            String [] param2Items = param2.split(",");
            int rowFrom;
            int colFrom;
            int rowTo;
            int colTo;
            if (param1Items.length == 2) {
                try {
                    rowFrom = Integer.parseInt(param1Items[0]);
                } catch (NumberFormatException e) {
                    throw new ResponseException(400, "Please enter a valid number for the row.");
                }
                colFrom = getNumFromLetter(param1Items[1]);
                if (colFrom == 0) {
                    throw new ResponseException(400, "Please enter a valid letter for the column");
                }
            }
            else {
                throw new ResponseException(400, "Invalid first coordinate, please try again.");
            }
            if (param2Items.length == 2) {
                try { rowTo = Integer.parseInt(param2Items[0]); }
                catch (NumberFormatException e) {
                    throw new ResponseException(400, "Please enter a valid number for the row.");
                }
                colTo = getNumFromLetter(param2Items[1]);
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
                if (params.length == 3) {
                    ChessPiece.PieceType type = null;
                    if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                        throw new ResponseException(400, "You can't promote a piece that isn't a pawn.");
                    }
                    switch (params[2].toLowerCase()) {
                        case "pawn" -> type = ChessPiece.PieceType.PAWN;
                        case "knight" -> type = ChessPiece.PieceType.KNIGHT;
                        case "queen" -> type = ChessPiece.PieceType.QUEEN;
                        case "bishop" -> type = ChessPiece.PieceType.BISHOP;
                        case "rook" -> type = ChessPiece.PieceType.ROOK;
                        default -> throw new ResponseException(400, "invalid promotion piece type");
                    }
                    return new ChessMove(startPosition, endPosition, type);
                }
                return new ChessMove(startPosition, endPosition, null);
            }
        }
        else {
            throw new ResponseException(400, "Invalid number of arguments");
        }
    }

    private void validateMove(ChessGame game, ChessMove move) throws InvalidMoveException {
        game.makeMove(move);
    }

    private void getUpdatedBoard() throws ResponseException {
        ListGamesResult gamesResult = server.list(authToken);
        ArrayList<GameData> games = gamesResult.games();
        for (GameData data : games) {
            int gameID = data.gameID();
            if (gameID == currGameID) {
                currGame = data.game();
                currBoard = data.game().getBoard();
            }
        }
    }

    public String resign() throws ResponseException {
        assertSignedIn();
        if (state != State.PLAYINGGAME) {
            return "You cannot resign if you are not playing a game.";
        }
        System.out.print("Are you sure you wish to resign? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        if (!result.equals("y") && !result.equals("n")) {
            return "Invalid response, canceled resignation process";
        }
        else if (result.equals("y")) {
            ws.resignGame(authToken, currGameID);
            state = State.SIGNEDIN;
            currGameID = 0;
            currGame = null;
            return "You have resigned the game.";
        }
        else {
            return "canceled resignation process.";
        }
    }

    public String move(String... params) throws ResponseException {
        assertSignedIn();
        if (state != State.PLAYINGGAME) {
            return "You cannot make a move if you aren't playing a game.";
        }
        ChessMove move;
        try {
            move = parseChessMove(params);
            getUpdatedBoard();
            if (currGame == null) {
                return "Unable to find the game in the database.";
            }
            try {
                validateMove(currGame, move);
            } catch (InvalidMoveException e) {
                return e.getMessage();
            }

            ws.makeMove(authToken, currGameID, move);
            getUpdatedBoard();
            return "Successfully made move";
        } catch (ResponseException e) {
            return e.getMessage();
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
        try {
            assertSignedIn();
        }
        catch (ResponseException e) {
            throw e;
        }
        if (state == State.PLAYINGGAME || state == State.WATCHINGGAME) {
            return "You cannot join or watch another game while you are already playing or watching.";
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

                currGameID = givenGameID;
                getUpdatedBoard();
                ChessGame.TeamColor color;
                if (params[1].equals("white")) {
                    color = ChessGame.TeamColor.WHITE;
                }
                else {
                    color = ChessGame.TeamColor.BLACK;
                }
                JoinGameResult result = server.join(authToken, givenGameID, color);
                if (result.message() == null) {
                    joinGetBoardSetState(color);
                    if (!inSession) {
                        ws = new WebSocketFacade(serverUrl, notificationHandler);
                        ws.joinGame(authToken, currGameID);
                        inSession = true;
                    }
                    else {
                        ws.joinGame(authToken, currGameID);
                    }
                    return "Successfully joined game";
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

    public String redraw() throws ResponseException {
        if (state == State.PLAYINGGAME || state == State.WATCHINGGAME) {
            getUpdatedBoard();
            return new StringyBoard(currBoard, teamColor).getBoard();
        }
        else {
            return "You can't redraw a board if you aren't watching or playing.";
        }
    }

    private void joinGetBoardSetState(ChessGame.TeamColor color) throws ResponseException {
        getUpdatedBoard();
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
                return "";
            }
        }
    }

    private String leave() throws ResponseException {
        if (state == State.SIGNEDOUT || state == State.SIGNEDIN) {
            return "You aren't watching or playing a game.";
        }
        else {
            state = State.SIGNEDIN;
            ws.leaveGame(authToken, currGameID);
            teamColor = null;
            currGame = null;
            currGameID = 0;
            inSession = false;
            return "No longer playing/watching game.";
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}