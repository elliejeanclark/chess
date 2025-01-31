package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "|" + getPiece(new ChessPosition(8, 1)) +
                "|" + getPiece(new ChessPosition(8, 2)) +
                "|" + getPiece(new ChessPosition(8, 3)) +
                "|" + getPiece(new ChessPosition(8, 4)) +
                "|" + getPiece(new ChessPosition(8, 5)) +
                "|" + getPiece(new ChessPosition(8, 6)) +
                "|" + getPiece(new ChessPosition(8, 7)) +
                "|" + getPiece(new ChessPosition(8, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(7, 1)) +
                "|" + getPiece(new ChessPosition(7, 2)) +
                "|" + getPiece(new ChessPosition(7, 3)) +
                "|" + getPiece(new ChessPosition(7, 4)) +
                "|" + getPiece(new ChessPosition(7, 5)) +
                "|" + getPiece(new ChessPosition(7, 6)) +
                "|" + getPiece(new ChessPosition(7, 7)) +
                "|" + getPiece(new ChessPosition(7, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(6, 1)) +
                "|" + getPiece(new ChessPosition(6, 2)) +
                "|" + getPiece(new ChessPosition(6, 3)) +
                "|" + getPiece(new ChessPosition(6, 4)) +
                "|" + getPiece(new ChessPosition(6, 5)) +
                "|" + getPiece(new ChessPosition(6, 6)) +
                "|" + getPiece(new ChessPosition(6, 7)) +
                "|" + getPiece(new ChessPosition(6, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(5, 1)) +
                "|" + getPiece(new ChessPosition(5, 2)) +
                "|" + getPiece(new ChessPosition(5, 3)) +
                "|" + getPiece(new ChessPosition(5, 4)) +
                "|" + getPiece(new ChessPosition(5, 5)) +
                "|" + getPiece(new ChessPosition(5, 6)) +
                "|" + getPiece(new ChessPosition(5, 7)) +
                "|" + getPiece(new ChessPosition(5, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(4, 1)) +
                "|" + getPiece(new ChessPosition(4, 2)) +
                "|" + getPiece(new ChessPosition(4, 3)) +
                "|" + getPiece(new ChessPosition(4, 4)) +
                "|" + getPiece(new ChessPosition(4, 5)) +
                "|" + getPiece(new ChessPosition(4, 6)) +
                "|" + getPiece(new ChessPosition(4, 7)) +
                "|" + getPiece(new ChessPosition(4, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(3, 1)) +
                "|" + getPiece(new ChessPosition(3, 2)) +
                "|" + getPiece(new ChessPosition(3, 3)) +
                "|" + getPiece(new ChessPosition(3, 4)) +
                "|" + getPiece(new ChessPosition(3, 5)) +
                "|" + getPiece(new ChessPosition(3, 6)) +
                "|" + getPiece(new ChessPosition(3, 7)) +
                "|" + getPiece(new ChessPosition(3, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(2, 1)) +
                "|" + getPiece(new ChessPosition(2, 2)) +
                "|" + getPiece(new ChessPosition(2, 3)) +
                "|" + getPiece(new ChessPosition(2, 4)) +
                "|" + getPiece(new ChessPosition(2, 5)) +
                "|" + getPiece(new ChessPosition(2, 6)) +
                "|" + getPiece(new ChessPosition(2, 7)) +
                "|" + getPiece(new ChessPosition(2, 8)) + "\n" +
                "|" + getPiece(new ChessPosition(1, 1)) +
                "|" + getPiece(new ChessPosition(1, 2)) +
                "|" + getPiece(new ChessPosition(1, 3)) +
                "|" + getPiece(new ChessPosition(1, 4)) +
                "|" + getPiece(new ChessPosition(1, 5)) +
                "|" + getPiece(new ChessPosition(1, 6)) +
                "|" + getPiece(new ChessPosition(1, 7)) +
                "|" + getPiece(new ChessPosition(1, 8)) + "\n";
    }

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    public ChessBoard(ChessBoard copy) {
        squares = Arrays.copyOf(copy.squares, copy.squares.length);
        for (int i = 0; i < 8; i++) {
            squares[i] = Arrays.copyOf(copy.squares[i], copy.squares[i].length);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1 ][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1 ][position.getColumn() - 1 ];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addPiece( new ChessPosition(8 ,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece( new ChessPosition(8 ,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece( new ChessPosition(8 ,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece( new ChessPosition(8 ,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece( new ChessPosition(8 ,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece( new ChessPosition(8 ,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece( new ChessPosition(8 ,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece( new ChessPosition(8 ,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        addPiece( new ChessPosition(7 ,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(7 ,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        addPiece( new ChessPosition(1 ,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece( new ChessPosition(1 ,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece( new ChessPosition(1 ,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece( new ChessPosition(1 ,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece( new ChessPosition(1 ,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece( new ChessPosition(1 ,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece( new ChessPosition(1 ,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece( new ChessPosition(1 ,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        addPiece( new ChessPosition(2 ,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece( new ChessPosition(2 ,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
    }
}
