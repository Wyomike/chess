package chess;

import java.util.Collection;

public class KingMovement implements ChessPieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start) {
        // Make chessmoves that can go +- 1 in each direction with promotion type null.
        return new Collection<ChessMove>();
    }
}
