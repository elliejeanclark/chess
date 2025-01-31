package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor activeTeam;
    ChessBoard board;

    public ChessGame() {
        this.activeTeam = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return activeTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        activeTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(startPosition);
        TeamColor color = currPiece.getTeamColor();
        if (currPiece == null) {
            return null;
        }
        else {
            potentialMoves.addAll(currPiece.pieceMoves(board, startPosition));
        }

        if (isInCheck(currPiece.getTeamColor())){
            for (ChessMove move : potentialMoves){
                ChessBoard updatedBoard = updateBoard(board, move);
                if (!inCheck(color, updatedBoard)){
                    validMoves.add(move);
                }
            }
        }
        else {
            for (ChessMove move : potentialMoves) {
                ChessBoard updatedBoard = updateBoard(board, move);
                if (!inCheck(color, updatedBoard)){
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null){
            throw new InvalidMoveException("there is not a piece there");
        }
        else if (currPiece.getTeamColor() != activeTeam){
            throw new InvalidMoveException("It is not your turn");
        }
        else {
            ArrayList<ChessMove> potentialMoves = new ArrayList<>();
            potentialMoves.addAll(validMoves(startPosition));
            boolean validMove = false;
            for (ChessMove pMove : potentialMoves) {
                if (pMove.equals(move)){
                    validMove = true;
                    break;
                }
            }

            if (validMove) {
                ChessBoard updatedBoard = updateBoard(board, move);
                setBoard(updatedBoard);
                if (activeTeam == TeamColor.WHITE){
                    activeTeam = TeamColor.BLACK;
                }
                else {
                    activeTeam = TeamColor.WHITE;
                }
            }
            else {
                throw new InvalidMoveException("That is not a valid move.");
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inCheck(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private ChessPosition getKing(ChessBoard board, TeamColor color) {
        int row = 1;
        int col = 1;
        ChessPosition kingPosition = new ChessPosition(row, col);
        while(row < 9) {
            while (col < 9) {
                ChessPosition currPosition = new ChessPosition(row, col);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece == null) {
                    col += 1;
                }
                else if (currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == color) {
                    kingPosition = currPosition;
                    return kingPosition;
                }
                else {
                    col += 1;
                }
            }
            col = 1;
            row += 1;
        }
        return kingPosition;
    }

    private Collection<ChessPosition> getTeamPositions (ChessBoard board, TeamColor color) {
        ArrayList<ChessPosition> allPositions = new ArrayList<>();
        int row = 1;
        int col = 1;
        while(row < 9) {
            while (col < 9) {
                ChessPosition currPosition = new ChessPosition(row, col);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece == null) {
                    col += 1;
                }
                else if (currPiece.getTeamColor() != color) {
                    col += 1;
                }
                else {
                    allPositions.add(currPosition);
                    col += 1;
                }
            }
            col = 1;
            row += 1;
        }

        return allPositions;
    }

    private ChessBoard updateBoard(ChessBoard oldBoard, ChessMove move) {
        ChessPiece currPiece = oldBoard.getPiece(move.getStartPosition());
        ChessBoard updatedBoard = new ChessBoard(oldBoard);
        ChessPiece promotionPiece = new ChessPiece(currPiece.getTeamColor(), move.getPromotionPiece());
        if (move.getPromotionPiece() == null){
            updatedBoard.addPiece(move.getEndPosition(), currPiece);
            updatedBoard.addPiece(move.getStartPosition(), null);
        }
        else {
            updatedBoard.addPiece(move.getEndPosition(), promotionPiece);
            updatedBoard.addPiece(move.getStartPosition(), null);
        }
        return updatedBoard;
    }

    private boolean inCheck (TeamColor color, ChessBoard board) {
        ChessPosition kingPosition = getKing(board, color);
        TeamColor otherTeamColor;
        if (color == TeamColor.WHITE) {
            otherTeamColor = TeamColor.BLACK;
        }
        else {
            otherTeamColor = TeamColor.WHITE;
        }

        Collection<ChessPosition> otherTeamPositions = getTeamPositions(board, otherTeamColor);
        for (ChessPosition position : otherTeamPositions) {
            ArrayList<ChessMove> oppValidMoves = new ArrayList<>();
            ChessPiece currPiece = board.getPiece(position);
            if (currPiece != null) {
                oppValidMoves.addAll(currPiece.pieceMoves(board, position));
            }
            for (ChessMove oppMove : oppValidMoves) {
                if (oppMove.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }
}
