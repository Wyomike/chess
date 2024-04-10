package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovement implements PieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        SlideMovement slideMovement = new SlideMovement();
        Collection<ChessMove> diag = slideMovement.diagonalMovemet(start, board);
        Collection<ChessMove> square = slideMovement.squareMovement(start, board);
        diag.addAll(square);
        return diag;
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        return new SlideMovement().isValidMove(start, end, board);
    }
}
