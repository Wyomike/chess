package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

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
