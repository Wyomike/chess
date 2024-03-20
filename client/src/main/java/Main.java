import Server.ServerFacade;
import chess.*;
import model.AuthData;
import ui.ChessBoardDraw;
import ui.Menu;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ChessBoard chess = new ChessBoard();
        chess.resetBoard();
        Menu menu = new Menu(chess, 8080);
        menu.run();
    }
}