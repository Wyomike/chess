package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor color;
    PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }
    public ChessPiece(ChessPiece piece) {
        this.color = piece.color;
        this.type = piece.type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(type) {
            case ROOK:
                return new RookMovement().validMoves(myPosition, board);
            case BISHOP:
                return new BishopMovement().validMoves(myPosition, board);
            case QUEEN:
                return new QueenMovement().validMoves(myPosition, board);
            case KING:
                return new KingMovement().validMoves(myPosition, board);
            case KNIGHT:
                return new KnightMovement().validMoves(myPosition, board);
            case PAWN:
                return new PawnMovement().validMoves(myPosition, board);
        }
        return new RookMovement().validMoves(myPosition, board);
    }

    @Override
    public String toString() {
        String colorStr = "";
        String typeStr = "";
        if (color == ChessGame.TeamColor.WHITE) colorStr = "w";
        else colorStr = "b";
        switch(type) {
            case ROOK:
                typeStr = "R";
            case BISHOP:
                typeStr = "B";
            case QUEEN:
                typeStr = "Q";
            case KING:
                typeStr = "K";
            case KNIGHT:
                typeStr = "H";
            case PAWN:
                typeStr = "P";
        }
        return colorStr + typeStr;
    }
}