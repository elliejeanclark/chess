package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

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

    public Collection<ChessMove> bishopMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
            boolean upRightClear = true;
            boolean upLeftClear = true;
            boolean downRightClear = true;
            boolean downLeftClear = true;
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

            while (upRightClear) {
                if (board.getPiece(nextPosition) == null) {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = nextPosition;
                    nextPosition = new ChessPosition(nextPosition.getRow() + 1, nextPosition.getColumn() + 1);
                    if (nextPosition.getRow() >= 8 || nextPosition.getColumn() >= 8) {
                        upRightClear = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = myPosition;
                    upRightClear = false;
                }
            }

            nextPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            while (upLeftClear) {
                if (board.getPiece(nextPosition) == null) {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = nextPosition;
                    nextPosition = new ChessPosition(nextPosition.getRow() + 1, nextPosition.getColumn() - 1);
                    if (nextPosition.getRow() >= 8 || nextPosition.getColumn() >= 8) {
                        upLeftClear = false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = myPosition;
                    upLeftClear = false;
                }
            }

            nextPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            while (downRightClear) {
                if (board.getPiece(nextPosition) == null) {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = nextPosition;
                    nextPosition = new ChessPosition(nextPosition.getRow() - 1, nextPosition.getColumn() + 1);
                    if (nextPosition.getRow() >= 8 || nextPosition.getColumn() >= 8) {
                        downRightClear= false;
                    }
                }
                else {
                    validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                    currentPosition = myPosition;
                    downRightClear = false;
                }
            }

        nextPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        while (downLeftClear) {
            if (board.getPiece(nextPosition) == null) {
                validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                currentPosition = nextPosition;
                nextPosition = new ChessPosition(nextPosition.getRow() - 1, nextPosition.getColumn() - 1);
                if (nextPosition.getRow() >= 8 || nextPosition.getColumn() >= 8 || nextPosition.getRow() < 0 || nextPosition.getColumn() < 0) {
                    downLeftClear= false;
                }
            }
            else {
                validMoves.add(new ChessMove(currentPosition, nextPosition, null));
                currentPosition = myPosition;
                downLeftClear = false;
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> kingMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate king movement here.
        return validMoves;
    }

    public Collection<ChessMove> knightMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate knight movement here.
        return validMoves;
    }

    public Collection<ChessMove> pawnMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate pawn movement here.
        return validMoves;
    }

    public Collection<ChessMove> queenMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate queen movement here.
        return validMoves;
    }

    public Collection<ChessMove> rookMovement(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //calculate rook movement here.
        return validMoves;
    }
}
