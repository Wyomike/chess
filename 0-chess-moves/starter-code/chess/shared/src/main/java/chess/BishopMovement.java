package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovement implements ChessPieceMovement {

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> pieceMoves = new HashSet<>();
        ChessPosition nextMove = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        while(validMove(nextMove, board, color)) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() + 1);
            nextMove.setCol(nextMove.getColumn() + 1);
        }
        nextMove.setRow(start.getRow() + 1);
        nextMove.setCol(start.getColumn() - 1);
        while(validMove(nextMove, board, color)) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() + 1);
            nextMove.setCol(nextMove.getColumn() - 1);
        }
        nextMove.setRow(start.getRow() - 1);
        nextMove.setCol(start.getColumn() - 1);
        while(validMove(nextMove, board, color)) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() - 1);
            nextMove.setCol(nextMove.getColumn() - 1);
        }
        nextMove.setRow(start.getRow() - 1);
        nextMove.setCol(start.getColumn() + 1);
        while(validMove(nextMove, board, color)) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() - 1);
            nextMove.setCol(nextMove.getColumn() + 1);
        }
        return pieceMoves;
    }

    public boolean validMove(ChessPosition end, ChessBoard board, ChessGame.TeamColor color) {
        if(end.getRow() > 8 || end.getColumn() > 8 || end.getRow() < 1 || end.getColumn() < 1) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            return board.getPiece(end).getTeamColor() != color;
        }
        else {
            return true;
        }
    }
}