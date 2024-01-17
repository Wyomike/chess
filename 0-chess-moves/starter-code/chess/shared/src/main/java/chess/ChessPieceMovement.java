package chess;

import java.util.Collection;
//MAKE THIS A TEMPLATE CLASS!
interface ChessPieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color);
    public boolean validMove(ChessPosition end, ChessBoard board, ChessGame.TeamColor color);
}
