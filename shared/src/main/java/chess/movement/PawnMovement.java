package chess.movement;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovement implements PieceMovement {
    boolean obstructed = true;

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<> ();

        if (board.getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) { //white
            ChessPosition end = new ChessPosition(start.getRow() + 1, start.getColumn());
            if (start.getRow() == 7) {
                ChessPiece.PieceType[] promotionList = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK};
                for (int i = 0; i < 4; ++i) {
                    end = new ChessPosition(start.getRow() + 1, start.getColumn());
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                    //capture
                    end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                    end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                }
            }
            else {
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                if (start.getRow() == 2 && board.getPiece(end) == null) {//start double
                    end = new ChessPosition(start.getRow() + 2, start.getColumn());
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                }
                //capture
                end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            }
        }
        else { //black
            ChessPosition end = new ChessPosition(start.getRow() - 1, start.getColumn());
            if (start.getRow() == 2) {
                ChessPiece.PieceType[] promotionList = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK};
                for (int i = 0; i < 4; ++i) {
                    end = new ChessPosition(start.getRow() - 1, start.getColumn());
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                    //capture
                    end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                    end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, promotionList[i]));
                }
            }
            else {
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                if (start.getRow() == 7 && board.getPiece(end) == null) {//start double
                    end = new ChessPosition(start.getRow() - 2, start.getColumn());
                    if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                }
                //capture
                end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                if (isValidMove(start, end, board)) moves.add(new ChessMove(start, end, null));
            }
        }

        return moves;
    }

    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        ChessGame.TeamColor ownColor = board.getPiece(start).getTeamColor();
        if (!isInBoard(end)) {
            return false;
        }
        if (start.getColumn() != end.getColumn()) {
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != ownColor) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (board.getPiece(end) != null) {
            return false;
        }
        return true;
    }
    public boolean isInBoard(ChessPosition position) {
        return position.getRow() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
}
