package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
            return bishopMovement(board, myPosition);
        }
        else if (type == PieceType.KING) {
            return kingMovement(board, myPosition);
        }
        else if (type == PieceType.KNIGHT) {
            return knightMovement(board, myPosition);
        }
        else if (type == PieceType.PAWN) {
            return pawnMovement(board, myPosition);
        }
        else if (type == PieceType.QUEEN) {
            return queenMovement(board, myPosition);
        }
        else if (type == PieceType.ROOK) {
            return rookMovement(board, myPosition);
        }
        else {
            throw new RuntimeException("I am not a valid chess piece. Now how did that happen?");
        }
    }

    private boolean inBounds(ChessPosition position) {
        if (position.getRow() > 8 || position.getColumn() > 8 || position.getRow() < 1 || position.getColumn() < 1) {
            return false;
        }
        return true;
    }

    private Collection<ChessMove> oneDirectionMovement(ChessBoard board, ChessPosition myPosition, int rowMovement, int colMovement) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        boolean clear = true;
        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowMovement, myPosition.getColumn() + colMovement);
        if (!inBounds(nextPosition)){
            clear = false;
        }

        while (clear) {
            if (board.getPiece(nextPosition) == null) {
                validMoves.add(new ChessMove(myPosition, nextPosition, null));
                nextPosition = new ChessPosition(nextPosition.getRow() + rowMovement, nextPosition.getColumn() + colMovement);
                if (!inBounds(nextPosition)) {
                    clear = false;
                }
            }
            else {
                if(pieceColor != board.getPiece(nextPosition).pieceColor) {
                    validMoves.add(new ChessMove(myPosition, nextPosition, null));
                }
                clear = false;
            }
        }

        return validMoves;
    }

    private ChessMove singleSquareMovement(ChessBoard board, ChessPosition myPosition, int rowMovement, int colMovement) {
        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowMovement, myPosition.getColumn() + colMovement);
        if(inBounds(nextPosition)) {
            if (board.getPiece(nextPosition) == null) {
                return new ChessMove(myPosition, nextPosition, null);
            }
            else {
                if (pieceColor != board.getPiece(nextPosition).pieceColor) {
                    return new ChessMove(myPosition, nextPosition, null);
                }
            }
        }
        return null;
    }

    private Collection<ChessMove> bishopMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        Collection<ChessMove> upRightMoves = oneDirectionMovement(board, myPosition, 1, 1);
        Collection<ChessMove> upLeftMoves = oneDirectionMovement(board, myPosition, -1, 1);
        Collection<ChessMove> downRightMoves = oneDirectionMovement(board, myPosition, 1, -1);
        Collection<ChessMove> downLeftMoves = oneDirectionMovement(board, myPosition, -1, -1);
        validMoves.addAll(upRightMoves);
        validMoves.addAll(upLeftMoves);
        validMoves.addAll(downRightMoves);
        validMoves.addAll(downLeftMoves);
        return validMoves;
    }

    private Collection<ChessMove> kingMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        potentialMoves.add(singleSquareMovement(board, myPosition, 1, 0));
        potentialMoves.add(singleSquareMovement(board, myPosition, 1, 1));
        potentialMoves.add(singleSquareMovement(board, myPosition, 1, -1));
        potentialMoves.add(singleSquareMovement(board, myPosition, 0, 1));
        potentialMoves.add(singleSquareMovement(board, myPosition, 0, -1));
        potentialMoves.add(singleSquareMovement(board, myPosition, -1, 1));
        potentialMoves.add(singleSquareMovement(board, myPosition, -1, -1));
        potentialMoves.add(singleSquareMovement(board, myPosition, -1, 0));

        for (ChessMove move : potentialMoves) {
            if (move != null) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> knightMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        potentialMoves.add(singleSquareMovement(board, myPosition, 2, 1));
        potentialMoves.add(singleSquareMovement(board, myPosition, 1, 2));
        potentialMoves.add(singleSquareMovement(board, myPosition, -2, 1));
        potentialMoves.add(singleSquareMovement(board, myPosition, -1, 2));
        potentialMoves.add(singleSquareMovement(board, myPosition, 2, -1));
        potentialMoves.add(singleSquareMovement(board, myPosition, 1, -2));
        potentialMoves.add(singleSquareMovement(board, myPosition, -2, -1));
        potentialMoves.add(singleSquareMovement(board, myPosition, -1, -2));

        for (ChessMove move : potentialMoves) {
            if (move != null) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> pawnMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate pawn movement here.
        return validMoves;
    }

    private Collection<ChessMove> queenMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> bishopMoves = bishopMovement(board, myPosition);
        Collection<ChessMove> rookMoves = rookMovement(board, myPosition);
        validMoves.addAll(bishopMoves);
        validMoves.addAll(rookMoves);
        return validMoves;
    }

    private Collection<ChessMove> rookMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> upMoves = oneDirectionMovement(board, myPosition, 1, 0);
        Collection<ChessMove> downMoves = oneDirectionMovement(board, myPosition, -1, 0);
        Collection<ChessMove> rightMoves = oneDirectionMovement(board, myPosition, 0, 1);
        Collection<ChessMove> leftMoves = oneDirectionMovement(board, myPosition, 0, -1);
        validMoves.addAll(upMoves);
        validMoves.addAll(downMoves);
        validMoves.addAll(rightMoves);
        validMoves.addAll(leftMoves);

        return validMoves;
    }
}
