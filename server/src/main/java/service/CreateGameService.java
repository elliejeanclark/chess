package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import reqandres.CreateGameRequest;
import reqandres.CreateGameResult;
import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class CreateGameService {

    private final CreateGameRequest req;
    private CreateGameResult res;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;

    public CreateGameService(CreateGameRequest req, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    private boolean checkAuthorized() {
        boolean authorized = false;
        String authToken = req.authToken();
        AuthData authData = authAccess.getAuth(authToken);
        if (authData != null) {
            authorized = true;
        }
        return authorized;
    }

    private boolean checkNotTaken() {
        String givenGameName = req.gameName();
        HashMap<Integer, GameData> games = gameAccess.getGames();
        for (GameData game : games.values()) {
            if (game.gameName().equals(givenGameName)) {
                return false;
            }
        }
        return true;
    }

    private int generateGameID() {
        int gameID = gameAccess.getNextID();
        return gameID;
    }

    public CreateGameResult createGame() {
        boolean authorized = checkAuthorized();
        boolean available = checkNotTaken();
        if (!authorized) {
            this.res = new CreateGameResult(null, "Error: unauthorized");
        }
        else if (!available) {
            this.res = new CreateGameResult( null, "Error: already Taken");
        }
        else {
            int gameID = generateGameID();
            gameAccess.createGame(gameID, null, null, req.gameName(), new ChessGame());
            this.res = new CreateGameResult(gameID, null);
        }
        return res;
    }
}
