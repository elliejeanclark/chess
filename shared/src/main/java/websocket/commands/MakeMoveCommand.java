package websocket.commands;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;

    public MakeMoveCommand (UserGameCommand.CommandType type, String authToken, int gameID, ChessMove move) {
        super(type, authToken, gameID);
        this.move = move;
    }

    public void setMove (ChessMove move) {
        this.move = move;
    }

    public ChessMove getMove () {
        return move;
    }
}
