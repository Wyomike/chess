package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovement implements ChessPieceMovement {

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> pieceMoves = new HashSet<>();
        //make a var to track position, make a new move for each of the 8 positions if valid.
        ChessPosition end = new ChessPosition(start.getRow() - 2, start.getColumn() + 1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() - 1);
        end.setCol(start.getColumn() + 2);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() + 1);
        end.setCol(start.getColumn() + 2);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() + 2);
        end.setCol(start.getColumn() + 1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() - 2);
        end.setCol(start.getColumn() - 1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() - 1);
        end.setCol(start.getColumn() - 2);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() + 1);
        end.setCol(start.getColumn() - 2);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

        end.setRow(start.getRow() + 2);
        end.setCol(start.getColumn() - 1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, new ChessPosition(end), null));

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