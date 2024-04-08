package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board = new ChessBoard();
    TeamColor turn = TeamColor.WHITE;
    boolean isDone = false;

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
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
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoveList = new ArrayList<ChessMove>();
        Iterator<ChessMove> moveIter = moves.iterator();
        while (moveIter.hasNext()) {
            ChessMove move = moveIter.next();
            if (isValidMove(move, board.getPiece(startPosition).getTeamColor())) validMoveList.add(move);
        }
        return validMoveList;
    }
    private ArrayList<ChessMove> validTeamMoves(TeamColor color) {
        ArrayList<ChessMove> teamMoves = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                ChessPosition check = new ChessPosition(i,j);
                if (validPiece(check) && board.getPiece(check).getTeamColor() == color) teamMoves.addAll(validMoves(check));
            }
        }
        return teamMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        if (color != getTeamTurn()) throw new InvalidMoveException("Invalid move");

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (!isInCheck(color)) {
            if (moves.contains(move)) {
                board.movePiece(move);
                if(getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
                else setTeamTurn(TeamColor.WHITE);
            }
            else throw new InvalidMoveException("Invalid move");
        }
        else {
            ChessBoard tempBoard = new ChessBoard(board);
            if (moves.contains(move)) {
                board.movePiece(move);
                if (isInCheck(color)) {
                    board = tempBoard;
                    throw new InvalidMoveException("Invalid move");
                }
                if(getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
                else setTeamTurn(TeamColor.WHITE);
            }
            else throw new InvalidMoveException("Not in vaild moveset");
        }
    }
    private boolean isValidMove(ChessMove move, TeamColor color) {
        ChessBoard tempBoard = new ChessBoard(board);
        board.movePiece(move);
        if (isInCheck(color)) {
            board = tempBoard;
            return false;
        }
        else {
            board = tempBoard;
            return true;
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
        if (kingLoc == null) return false;
        boolean danger = false;
        if (dangerKnight(kingLoc)) danger = true;
        if (dangerRook(kingLoc)) danger = true; //STILL NEED TO IMPLEMENT BISHOP AND PAWN
        if (dangerBishop(kingLoc)) danger = true;
        if (dangerPawn(kingLoc)) danger = true;
        if (dangerKing(kingLoc)) danger = true;
        return danger;
    }
    private boolean dangerKnight(ChessPosition position) {
        TeamColor color = board.getPiece(position).getTeamColor();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -2; j <= 2; j += 4) {
                ChessPosition check1 = new ChessPosition(position.getRow() + i, position.getColumn() + j);
                ChessPosition check2 = new ChessPosition(position.getRow() + j, position.getColumn() + i);
                if (validPiece(check1) && board.getPiece(check1).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(check1).getTeamColor() != color) return true;
                if (validPiece(check2) && board.getPiece(check2).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(check2).getTeamColor() != color) return true;
            }
        }
        return false;
    }
    private boolean dangerRook(ChessPosition position) {//Rook and Bishop check for queen because queen = bishop + rook
        TeamColor color = board.getPiece(position).getTeamColor();

        boolean obstructs = false;
        for (int i = 1; i < 7; i += 1) {
            ChessPosition check = new ChessPosition(position.getRow() + i,position.getColumn());
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.ROOK ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow() - i,position.getColumn());
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.ROOK ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow(),position.getColumn() + i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.ROOK ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow(),position.getColumn() - i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.ROOK ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        return false;
    }
    private boolean dangerBishop(ChessPosition position) {
        boolean obstructs = false;
        TeamColor color = board.getPiece(position).getTeamColor();
        for (int i = 1; i < 7; i += 1) {
            ChessPosition check = new ChessPosition(position.getRow() + i,position.getColumn() - i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.BISHOP ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow() + i,position.getColumn() + i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.BISHOP ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow() - i,position.getColumn() - i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.BISHOP ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        obstructs = false;
        for (int i = 1; i < 7; ++i) {
            ChessPosition check = new ChessPosition(position.getRow() - i,position.getColumn() + i);
            if (validPiece(check) && (board.getPiece(check).getPieceType() == ChessPiece.PieceType.BISHOP ||
                    board.getPiece(check).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                    board.getPiece(check).getTeamColor() != color && !obstructs) return true;
            if (validPiece(check)) obstructs = true;
        }
        return false;
    }
    public boolean dangerPawn(ChessPosition position) {
        TeamColor color = board.getPiece(position).getTeamColor();
        int direction = 0;
        if (color == TeamColor.WHITE) direction = 1;
        else direction = -1;
        ChessPosition checkRight = new ChessPosition(position.getRow() + direction,position.getColumn() + 1);
        ChessPosition checkLeft = new ChessPosition(position.getRow() + direction,position.getColumn() - 1);
        if (validPiece(checkRight) && board.getPiece(checkRight).getPieceType() == ChessPiece.PieceType.PAWN &&
                board.getPiece(checkRight).getTeamColor() != color) return true;
        if (validPiece(checkLeft) && board.getPiece(checkLeft).getPieceType() == ChessPiece.PieceType.PAWN &&
                board.getPiece(checkLeft).getTeamColor() != color) return true;

        return false;
    }
    public boolean dangerKing(ChessPosition position) {
        TeamColor color = board.getPiece(position).getTeamColor();
        for (int i = -1; i <= 1; ++i) {
            ChessPosition checkUp = new ChessPosition(position.getRow() + 1,position.getColumn() + i);
            ChessPosition checkDown = new ChessPosition(position.getRow() - 1,position.getColumn() - i);
            if (checkDanger(color, checkUp, checkDown)) return true;
        }
        ChessPosition checkRight = new ChessPosition(position.getRow(),position.getColumn() + 1);
        ChessPosition checkLeft = new ChessPosition(position.getRow(),position.getColumn() - 1);
        if (checkDanger(color, checkRight, checkLeft)) return true;

        return false;
    }
    private boolean checkDanger(TeamColor color, ChessPosition checkUp, ChessPosition checkDown) {
        if (validPiece(checkUp) && board.getPiece(checkUp).getPieceType() == ChessPiece.PieceType.KING &&
                board.getPiece(checkUp).getTeamColor() != color) return true;
        if (validPiece(checkDown) && board.getPiece(checkDown).getPieceType() == ChessPiece.PieceType.KING &&
                board.getPiece(checkDown).getTeamColor() != color) return true;
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) { //Check check of all possible next turn boards?
        ArrayList<ChessMove> teamMoves = validTeamMoves(teamColor);
        boolean noValidMoves = true;
        Iterator<ChessMove> listIterator = teamMoves.iterator();
        while (listIterator.hasNext()) {
            if (isValidMove(listIterator.next(), teamColor)) {
                noValidMoves = false;
                return noValidMoves;
            }
        }
        setDone();
        return noValidMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor) && getTeamTurn() == teamColor) return true;
        else return isDone;
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
     * if the space at position is a valid piece
     */
    private boolean validPiece(ChessPosition position) {
        return isInBoard(position) && board.getPiece(position) != null;
    }
    private boolean isInBoard(ChessPosition position) {
        String test = board.toString();
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

    public boolean isDone() {
        return isDone;
    }

    public void setDone() {
        isDone = true;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                ", isDone=" + isDone +
                '}';
    }
}
