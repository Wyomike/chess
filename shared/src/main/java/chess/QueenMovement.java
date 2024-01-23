package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovement implements ChessPieceMovement {
    private boolean pieceObstructs;

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> pieceMoves = new HashSet<>();
        //Literally just bishop + rook code
        ChessPosition nextMove = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
        boolean obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs; //to allow a move to be added, then to stop adding.
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() + 1);
            nextMove.setCol(nextMove.getColumn() + 1);
        }
        nextMove.setRow(start.getRow() + 1);
        nextMove.setCol(start.getColumn() - 1);
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() + 1);
            nextMove.setCol(nextMove.getColumn() - 1);
        }
        nextMove.setRow(start.getRow() - 1);
        nextMove.setCol(start.getColumn() - 1);
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() - 1);
            nextMove.setCol(nextMove.getColumn() - 1);
        }
        nextMove.setRow(start.getRow() - 1);
        nextMove.setCol(start.getColumn() + 1);
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() - 1);
            nextMove.setCol(nextMove.getColumn() + 1);
        }
        nextMove.setRow(start.getRow() + 1); //Have to put these four lines in that aren't there normally in rook.
        nextMove.setCol(start.getColumn());
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs; //to allow a move to be added, then to stop adding.
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() + 1);
            nextMove.setCol(nextMove.getColumn());
        }
        nextMove.setRow(start.getRow() - 1);
        nextMove.setCol(start.getColumn());
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow() - 1);
            nextMove.setCol(nextMove.getColumn());
        }
        nextMove.setRow(start.getRow());
        nextMove.setCol(start.getColumn() + 1);
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow());
            nextMove.setCol(nextMove.getColumn() + 1);
        }
        nextMove.setRow(start.getRow());
        nextMove.setCol(start.getColumn() - 1);
        obstructed = false;
        pieceObstructs = false;
        while(validMove(nextMove, board, color) && !obstructed) {
            ChessPosition end = new ChessPosition(nextMove.getRow(), nextMove.getColumn());
            obstructed = pieceObstructs;
            pieceMoves.add(new ChessMove(start, end, null));
            nextMove.setRow(nextMove.getRow());
            nextMove.setCol(nextMove.getColumn() - 1);
        }

        return pieceMoves;
    }

    public boolean validMove(ChessPosition end, ChessBoard board, ChessGame.TeamColor color) {
        if(end.getRow() > 8 || end.getColumn() > 8 || end.getRow() < 1 || end.getColumn() < 1) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            if (board.getPiece(end).getTeamColor() != color) {
                pieceObstructs = true;
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
}