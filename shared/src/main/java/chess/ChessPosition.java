package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int posRow = 0;
    private int posCol = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return posRow == that.posRow && posCol == that.posCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posRow, posCol);
    }

    public ChessPosition(int row, int col) {
        posRow = row;
        posCol = col;
    }
    public ChessPosition(ChessPosition position) {
        posRow = position.getRow();
        posCol = position.getColumn();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return(posRow);
    }
    public void setRow(int row){posRow = row;}

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return(posCol);
    }
    public void setCol(int col){posCol = col;}
}
