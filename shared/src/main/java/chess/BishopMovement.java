package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovement implements PieceMovement {
    boolean obstructed = false;
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<> ();
        ChessPosition tempEnd = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        while (!obstructed && isInBoard(tempEnd)) { //up right
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() + 1, tempEnd.getColumn() + 1);
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
        while (!obstructed && isInBoard(tempEnd)) { //down right
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() - 1, tempEnd.getColumn() + 1);
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
        while (!obstructed && isInBoard(tempEnd)) { //up left
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() + 1, tempEnd.getColumn() - 1);
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
        while (!obstructed && isInBoard(tempEnd)) { //down left
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() - 1, tempEnd.getColumn() - 1);
        }

        return moves;
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        if (!isInBoard(end)) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            obstructed = true;
            if (board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor()) return true;
            else return false;
        }
        return true;
    }
    public boolean isInBoard(ChessPosition position) {
        return position.getRow() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
}
