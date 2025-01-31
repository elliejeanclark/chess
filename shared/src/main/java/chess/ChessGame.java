package chess;

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
        Collection<ChessMove> potentialMoves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(startPosition);
        ChessPosition kingPosition = getKing(board, currPiece.getTeamColor());
        if (currPiece == null) {
            return null;
        } else {
            potentialMoves.addAll(currPiece.pieceMoves(board, startPosition));
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKing(board, teamColor);
        TeamColor otherTeamColor;
        if (teamColor == TeamColor.WHITE) {
            otherTeamColor = TeamColor.BLACK;
        }
        else {
            otherTeamColor = TeamColor.WHITE;
        }

        Collection<ChessPosition> otherTeamPositions = getTeamPositions(board, otherTeamColor);
        for (ChessPosition position : otherTeamPositions) {
            Collection<ChessMove> opposingPieceMoves = validMoves(position);
            for (ChessMove oppMove : opposingPieceMoves) {
                if (oppMove.getEndPosition() == kingPosition) {
                    return true;
                }
            }
        }

        return false;
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
        ChessPosition currPosition = new ChessPosition(row, col);
        while(row < 9) {
            while (col < 9) {
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null) {
                    col += 1;
                }
                else if (currPiece.getPieceType() != ChessPiece.PieceType.KING && currPiece.getTeamColor() != color) {
                    col += 1;
                }
                else {
                    return currPosition;
                }
            }
            col = 1;
            row += 1;
        }
        return currPosition;
    }

    private Collection<ChessPosition> getTeamPositions (ChessBoard board, TeamColor color) {
        ArrayList<ChessPosition> allPositions = new ArrayList<>();
        int row = 1;
        int col = 1;
        ChessPosition currPosition = new ChessPosition(row, col);
        while(row < 9) {
            while (col < 9) {
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece == null) {
                    col += 1;
                }
                else if (currPiece.getTeamColor() != color) {
                    col +=1;
                }
                else {
                    allPositions.add(currPosition);
                }
            }
        }

        return allPositions;
    }
}
