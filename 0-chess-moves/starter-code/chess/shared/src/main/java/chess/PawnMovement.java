package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovement implements ChessPieceMovement {

    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> pieceMoves = new HashSet<>();
        if (color == ChessGame.TeamColor.WHITE) {
            if (start.getRow() == 2) {
                ChessPosition end = new ChessPosition(start.getRow() + 2, start.getColumn());
                ChessPosition between = new ChessPosition(start.getRow() + 1, start.getColumn());
                if (validMove(start, end, board, color) && board.getPiece(between) == null) pieceMoves.add(new ChessMove(start, end, null));
            }
            if (start.getRow() == 7) {
                ChessPiece.PieceType[] promotionType = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                        ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
                ChessPosition end = new ChessPosition(start.getRow() + 1, start.getColumn());
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
                end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
                end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
            }
            else {
                ChessPosition end = new ChessPosition(start.getRow() + 1, start.getColumn());
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
            }
        }
        //BLACK
        if (color == ChessGame.TeamColor.BLACK) {
            if (start.getRow() == 7) {
                ChessPosition between = new ChessPosition(start.getRow() - 1, start.getColumn());
                ChessPosition end = new ChessPosition(start.getRow() - 2, start.getColumn());
                if (validMove(start, end, board, color) && board.getPiece(between) == null) pieceMoves.add(new ChessMove(start, end, null));
            }
            if (start.getRow() == 2) {
                ChessPiece.PieceType[] promotionType = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                        ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
                ChessPosition end = new ChessPosition(start.getRow() - 1, start.getColumn());
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
                end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
                end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                for (int i = 0; i < 4; ++i) {
                    if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, promotionType[i]));
                }
            }
            else {
                ChessPosition end = new ChessPosition(start.getRow() - 1, start.getColumn());
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));
                end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                if (validMove(start, end, board, color)) pieceMoves.add(new ChessMove(start, end, null));

            }
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
    public boolean validMove(ChessPosition start, ChessPosition end, ChessBoard board, ChessGame.TeamColor color) {
        if(end.getRow() > 8 || end.getColumn() > 8 || end.getRow() < 1 || end.getColumn() < 1) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            if (start.getColumn() == end.getColumn()) {
                return false;
            }
            else {
                return board.getPiece(end).getTeamColor() != color;
            }
        }
        else {
            return start.getColumn() == end.getColumn();
        }
    }
}