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
        //Server server = new Server();
        //server.run(8080);
        Menu menu = new Menu(chess, 8080);
        //ChessBoardDraw draw = new ChessBoardDraw(chess);
        //draw.drawBoth();
        menu.run();
//        ServerFacade facade = new ServerFacade(8080);
//        System.out.println("??");
//        try {
//            facade.clear();
//            facade.register("1","1","1");
//            System.out.print("WUT");
//            AuthData auth = facade.register("2","2","2");
//            System.out.println(facade.createGame("game", auth.authToken()));
//            facade.joinGame("BLACK", 1, auth.authToken());
//            System.out.println(facade.listGames(auth.authToken()));
//            System.out.print(facade.listGames("heck"));
//        }
//        catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
    }
}