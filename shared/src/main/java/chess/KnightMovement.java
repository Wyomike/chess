package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovement implements PieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<> ();
        ChessPosition end = new ChessPosition(start.getRow() + 2, start.getColumn() + 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() + 2, start.getColumn() - 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() + 1, start.getColumn() - 2);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() + 1, start.getColumn() + 2);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 1, start.getColumn() + 2);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 1, start.getColumn() - 2);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 2, start.getColumn() + 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow() - 2, start.getColumn() - 1);
        if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
        return moves;
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        return new SingleMovement().isValidMove(start, end, board);
    }
}
