package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import reqandres.JoinGameRequest;
import reqandres.JoinGameResult;
import chess.ChessGame;

public class JoinGameService {

    private final JoinGameRequest req;
    private JoinGameResult res;
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;

    public JoinGameService(JoinGameRequest req, AuthDataAccess authAccess, GameDataAccess gameAccess) {
        this.req = req;
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
    }

    private boolean checkNotTaken() {
        ChessGame.TeamColor givenColor = req.playerColor();
        try {
            GameData game = gameAccess.getGame(req.gameID());
            if (givenColor == ChessGame.TeamColor.BLACK && game.blackUsername() == null) {
                return true;
            }
            else if (givenColor == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
                return true;
            }
        }
        catch (DataAccessException e) {
            return false;
        }
        return false;
    }

    private boolean checkAuthorized() {
        String authToken = req.authToken();
        AuthData authData = authAccess.getAuth(authToken);
        if (authData != null) {
            return true;
        }
        return false;
    }

    public JoinGameResult joinGame() {
        boolean authorized = checkAuthorized();
        boolean notTaken = checkNotTaken();
        if (!authorized) {
            this.res = new JoinGameResult("Error: unauthorized");
        }
        else if (req.playerColor() == null) {
            this.res = new JoinGameResult("Error: bad request");
        }
        else if (!notTaken) {
            this.res = new JoinGameResult("Error: already Taken");
        }
        else {
            try {
                AuthData authData = authAccess.getAuth(req.authToken());
                String username = authData.username();
                gameAccess.setPlayer(req.playerColor(), username, req.gameID());
                this.res = new JoinGameResult(null);
            }
            catch (DataAccessException e) {
                this.res = new JoinGameResult(e.getMessage());
            }
        }
        return res;
    }
}
