package chess;

import java.util.*;

public class KingMovement implements ChessPieceMovement {
    public Collection<ChessMove> validMoves(ChessPosition start) {
        Collection<ChessMove> pieceMoves = new ArrayList<ChessMove>();
        //for move forwards
        for (int i = -1; i <= 1; ++i) {
            ChessPosition end = new ChessPosition(start.getRow()+1, start.getColumn()+i);
            if (inBoard(end)) pieceMoves.add(new ChessMove(start, end, null));
        }
        for (int i = -1; i <= 1; ++i) { //for move backwards
            ChessPosition end = new ChessPosition(start.getRow()-1, start.getColumn()+i);
            if (inBoard(end)) pieceMoves.add(new ChessMove(start, end, null));
        }

        //for move left or right
        ChessPosition end = new ChessPosition(start.getRow(), start.getColumn()+1);
        if (inBoard(end)) pieceMoves.add(new ChessMove(start, end, null));
        end = new ChessPosition(start.getRow(), start.getColumn()-1);
        if (inBoard(end)) pieceMoves.add(new ChessMove(start, end, null));
        // Make chessmoves that can go +- 1 in each direction with promotion type null.
        return pieceMoves;
    }

    public boolean inBoard(ChessPosition end) {
        if(end.getRow() > 7 || end.getColumn() > 7) {
            return false;
        }
        else {
            return true;
        }
    }
}