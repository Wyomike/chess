package chess.movement;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
//MAKE THIS A TEMPLATE CLASS!
interface ChessPieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color);
    public boolean validMove(ChessPosition end, ChessBoard board, ChessGame.TeamColor color);
}
