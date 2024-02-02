package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board = new ChessPiece[8][8];
    //ChessPiece[] whitePieces = new ChessPiece[12]; //actually a list would probably be better...
    //ChessPiece[] blackPieces = new ChessPiece[12];
    //ChessPosition whiteKing = new ChessPosition(0, 4);
    //ChessPosition blackKing = new ChessPosition(7, 4);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard() {
    }
    public ChessBoard(ChessBoard copyFrom) {
        for (int i = 0; i < 8; ++i) {
            System.arraycopy(copyFrom.board[i], 0, board[i], 0, 8);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void movePiece(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotion = move.getPromotionPiece();
        ChessGame.TeamColor color = getPiece(start).getTeamColor();
        ChessPiece.PieceType type = getPiece(start).getPieceType();
        if (promotion == null) promotion = type;
        board[end.getRow() - 1][end.getColumn() - 1] = new ChessPiece(color, promotion);//getPiece(start);
        board[start.getRow() - 1][start.getColumn() - 1] = null;
        ChessPiece temp = board[end.getRow() - 1][end.getColumn() - 1];
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        for (int i = 0; i < 8; ++i) {
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        for (int i = 0; i < 8; ++i) {
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    public ChessPosition getKing(ChessGame.TeamColor color) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if(board[i][j] != null && board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == color) {
                    return new ChessPosition(i + 1,j + 1);
                }
            }
        }
        return null; //shouldn't ever happen.
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder();
        for (int i = 7; i >= 0; --i) {
            for (int j = 0; j < 8; ++j) {
                boardStr.append(" ");
                if (board[i][j] == null) {
                    boardStr.append("..");
                }
                else {
                    boardStr.append(board[i][j].toString());
                }
                boardStr.append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }
}
