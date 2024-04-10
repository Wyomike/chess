package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovement implements PieceMovement {
    boolean obstructed = false;
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        return new SlideMovement().diagonalMovemet(start, board);
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        return new SlideMovement().isValidMove(start, end, board);
    }
}
