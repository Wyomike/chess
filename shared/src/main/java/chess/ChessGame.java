package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor turn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
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
        ChessPiece piece = new ChessPiece(board.getPiece(startPosition));
        return piece.pieceMoves(board, startPosition);
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor color = board.getPiece(move.start).getTeamColor();
        if (!isInCheck(color)) {
            board.movePiece(move.getStartPosition(), move.getEndPosition());
        }
        else {
            throw new RuntimeException("Not implemented");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) { //Check diags, horizontal and veritcal, and knight pos relative to king?
        ChessPosition kingLoc = board.getKing(teamColor);
        boolean danger = false;
        if (dangerKnight(kingLoc)) danger = true;
        return danger;


        //throw new RuntimeException("Not implemented");
    }
    private boolean dangerKnight(ChessPosition position) { //think about just doing an iterable piece list...
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -2; j <= 2; j += 2) {
                ChessPosition check1 = new ChessPosition(position.getRow() + i, position.getColumn() + j);
                ChessPosition check2 = new ChessPosition(position.getRow() + j, position.getColumn() + i);
                if (validPiece(check1) && board.getPiece(check1).getPieceType() == ChessPiece.PieceType.KNIGHT) return true;
                if (validPiece(check2) && board.getPiece(check2).getPieceType() == ChessPiece.PieceType.KNIGHT) return true;
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
    public boolean isInCheckmate(TeamColor teamColor) { //Check check of all possible next turn boards?
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
        throw new RuntimeException("Not implemented");//honestly sei la
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    private boolean validPiece(ChessPosition position) {
        return isInBoard(position) && board.getPiece(position) != null;
    }
    private boolean isInBoard(ChessPosition position) {
        return position.getRow() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
