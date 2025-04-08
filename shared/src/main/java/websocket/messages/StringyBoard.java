package websocket.messages;
import chess.*;
import static model.EscapeSequences.*;

public class StringyBoard {
    private String stringifiedBoard;
    private ChessGame.TeamColor teamColor;

    public StringyBoard(ChessBoard board, ChessGame.TeamColor color) {
        this.teamColor = color;
        this.stringifiedBoard = stringifyBoard(board);
    }

    public String getBoard() {
        return stringifiedBoard;
    }

    private String stringifyBoard(ChessBoard board) {
        boolean whiteView = false;
        String result = "";
        if (teamColor == ChessGame.TeamColor.WHITE) {
            whiteView = true;
        }

        if (whiteView) {
            for (int i = 9; i >= 0; i--) {
                if (i == 0 || i == 9) {
                    result += RESET_TEXT_COLOR;
                    result += SET_BG_COLOR_MAGENTA;
                    result += "    a  b  c  d  e  f  g  h    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i, whiteView);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        else {
            for (int i = 0; i <= 9; i++) {
                if (i == 0 || i == 9) {
                    result += RESET_TEXT_COLOR;
                    result += SET_BG_COLOR_MAGENTA;
                    result += "    h  g  f  e  d  c  b  a    ";
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
                else {
                    result += drawRow(board, i, whiteView);
                    result += RESET_BG_COLOR;
                    result += "\n";
                }
            }
        }
        return result;
    }

    private String drawRow(ChessBoard board, int i, boolean whiteView) {
        String row = "";
        row += drawHeader(i);
        if (i % 2 == 0) {
            row += rowGuts(board, i, whiteView, false);
        }
        else {
            row += rowGuts(board, i, whiteView, true);
        }
        row += drawHeader(i);
        return row;
    }

    private String drawHeader(int i) {
        String row = "";
        row += SET_BG_COLOR_MAGENTA;
        row += " ";
        row += i;
        row += " ";
        return row;
    }

    private String rowGuts(ChessBoard board, int i, boolean whiteView, boolean oddRow) {
        String row = "";
        String oddCol;
        String evenCol;
        if (oddRow) {
            oddCol = SET_BG_COLOR_BLACK;
            evenCol = SET_BG_COLOR_WHITE;
        }
        else {
            oddCol = SET_BG_COLOR_WHITE;
            evenCol = SET_BG_COLOR_BLACK;
        }

        if (whiteView) {
            for (int j = 1; j <= 8; j++) {
                row += getSquare(board, i, j, oddCol, evenCol);
            }
        }
        else {
            for (int j = 8; j >= 1; j--) {
                row += getSquare(board, i, j, oddCol, evenCol);
            }
        }
        return row;
    }

    private String getSquare(ChessBoard board, int i, int j, String oddCol, String evenCol) {
        String row = "";
        if (j % 2 != 0) {
            row += oddCol;
            row += " ";
            row += getPieceColor(board, i, j);
            row += getPieceType(board, i, j);
            row += RESET_TEXT_COLOR;
            row += " ";
        }
        else {
            row += evenCol;
            row += " ";
            row += getPieceColor(board, i, j);
            row += getPieceType(board, i, j);
            row += RESET_TEXT_COLOR;
            row += " ";
        }
        return row;
    }

    private String getPieceType(ChessBoard board, int i, int j) {
        ChessPosition position = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return " ";
        }
        else {
            ChessPiece.PieceType pieceType = piece.getPieceType();
            return switch (pieceType) {
                case ChessPiece.PieceType.KING -> "K";
                case ChessPiece.PieceType.QUEEN -> "Q";
                case ChessPiece.PieceType.PAWN -> "P";
                case ChessPiece.PieceType.ROOK -> "R";
                case ChessPiece.PieceType.KNIGHT -> "N";
                case ChessPiece.PieceType.BISHOP -> "B";
            };
        }
    }

    private String getPieceColor(ChessBoard board, int i, int j) {
        ChessPosition position = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return "";
        }
        else {
            ChessGame.TeamColor color = piece.getTeamColor();
            return switch (color) {
                case ChessGame.TeamColor.WHITE -> SET_TEXT_COLOR_RED;
                case ChessGame.TeamColor.BLACK -> SET_TEXT_COLOR_BLUE;
            };
        }
    }
}
