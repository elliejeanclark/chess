import chess.*;
import server.*;

public class Main {
    Server testServer = new Server();
    //testServer.run(8080);
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}