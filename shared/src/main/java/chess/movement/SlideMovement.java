package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class SlideMovement {

    private boolean obstructed = false;

    public Collection<ChessMove> diagonalMovemet(ChessPosition start, ChessBoard board) {
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

    public Collection<ChessMove> squareMovement(ChessPosition start, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<> ();
        ChessPosition tempEnd = new ChessPosition(start.getRow() + 1, start.getColumn());
        while (!obstructed && isInBoard(tempEnd)) { //up
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() + 1, tempEnd.getColumn());
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow() - 1, start.getColumn());
        while (!obstructed && isInBoard(tempEnd)) { //down
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow() - 1, tempEnd.getColumn());
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow(), start.getColumn() + 1);
        while (!obstructed && isInBoard(tempEnd)) { //right
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn() + 1);
        }
        obstructed = false;
        tempEnd = new ChessPosition(start.getRow(), start.getColumn() - 1);
        while (!obstructed && isInBoard(tempEnd)) { //left
            ChessPosition end = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn());
            if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            tempEnd = new ChessPosition(tempEnd.getRow(), tempEnd.getColumn() - 1);
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
