import chess.*;
import ui.ChessBoardDraw;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ChessBoard chess = new ChessBoard();
        chess.resetBoard();
        Menu menu = new Menu(chess);
        //ChessBoardDraw draw = new ChessBoardDraw(chess);
        //draw.drawBoth();
        menu.run();
    }
}