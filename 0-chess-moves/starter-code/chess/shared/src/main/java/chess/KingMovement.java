package chess;

import java.util.*;

public class KingMovement implements ChessPieceMovement {

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> pieceMoves = new HashSet<>();
        //for move forwards
        for (int i = -1; i <= 1; ++i) {
            ChessPosition end = new ChessPosition(start.getRow()+1, start.getColumn()+i);

            if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
        }
        for (int i = -1; i <= 1; ++i) { //for move backwards
            ChessPosition end = new ChessPosition(start.getRow()-1, start.getColumn()+i);
            if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
        }

        //for move left or right
        ChessPosition end = new ChessPosition(start.getRow(), start.getColumn()+1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow(), start.getColumn()-1);
        if (validMove(end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
        // Make chessmoves that can go +- 1 in each direction with promotion type null.
        //for (int i = 0; i < pieceMoves.size(); ++i) {
            //System.out(pieceMoves.toString());
        //}
        return pieceMoves;
    }

    public boolean validMove(ChessPosition end, ChessBoard board, ChessGame.TeamColor color) {
        if(end.getRow() > 7 || end.getColumn() > 7 || end.getRow() < 0 || end.getColumn() < 0) {
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