package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType promotionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        System.out.println(this.toString());
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && promotionType == chessMove.promotionType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotionType);
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        promotionType = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return(start);
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return(end);
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return(promotionType);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start.getRow() + "," + start.getColumn() +
                ", end=" + end.getRow() + "," + end.getColumn() +
                ", promotionType=" + promotionType +
                '}';
    }
}
