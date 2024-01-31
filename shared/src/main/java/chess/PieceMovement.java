package chess;

import java.util.Collection;

public interface PieceMovement {
    Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board);
    boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board);
}
