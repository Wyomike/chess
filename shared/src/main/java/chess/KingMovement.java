package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovement implements PieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<> ();
        ChessPosition end = new ChessPosition(start.getRow() + 1, start.getColumn());
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow(), start.getColumn() - 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow(), start.getColumn() + 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 1, start.getColumn());
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));

        return moves;
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        if (!isInBoard(end)) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            if (board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor()) return true;
            else return false;
        }
        return true;
    }
    public boolean isInBoard(ChessPosition position) {
        return position.getRow() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
}
