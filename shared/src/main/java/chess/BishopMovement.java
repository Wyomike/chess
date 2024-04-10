package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovement implements PieceMovement {
    boolean obstructed = false;
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        return new SlideMovement().diagonalMovemet(start, board);
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        return new SlideMovement().isValidMove(start, end, board);
    }
}
