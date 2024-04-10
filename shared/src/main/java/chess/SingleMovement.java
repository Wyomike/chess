package chess;

public class SingleMovement {
    public boolean isValidMove(ChessPosition start, ChessPosition end, ChessBoard board) {
        if (!isInBoard(end)) {
            return false;
        }
        else if (board.getPiece(end) != null) {
            if (board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor()) return true;
            else return false;
        }
        return true;
    }
    public boolean isInBoard(ChessPosition position) {
        return position.getRow() <= 8 && position.getRow() >= 1 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
}
