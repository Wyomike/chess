package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor teamColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        teamColor = pieceColor;
        pieceType = type;
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
     * @return Which team this chess piece belongs to DONE
     */
    public ChessGame.TeamColor getTeamColor() {
        return(teamColor);
    }

    /**
     * @return which type of chess piece this piece is DONE
     */
    public PieceType getPieceType() {
        return(pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(board.getPiece(myPosition).getPieceType()) {
            case PieceType.PAWN: //DO STUFF
                break;
            case PieceType.KNIGHT: //CODE
                break;
        }
        throw new RuntimeException("Not implemented"); //LEAVE FOR LAST
    }
}