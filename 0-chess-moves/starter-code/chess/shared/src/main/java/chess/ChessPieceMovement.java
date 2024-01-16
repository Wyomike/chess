package chess;

import java.util.Collection;
//MAKE THIS A TEMPLATE CLASS!
interface ChessPieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start);
    public boolean inBoard(ChessPosition end);
}
