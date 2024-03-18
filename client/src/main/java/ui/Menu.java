package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Menu {//This is client? maybe I should refactor it to that.
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private Scanner scanner = new Scanner(System.in);
    private ChessBoardDraw boardDraw;

    public Menu(ChessBoard chess) {
        boardDraw = new ChessBoardDraw(chess);
    }

    public void run() {
        initial();
    }

    public void initial() {
        out.print("1.\tRegister\n");
        out.print("2.\tLogin\n");
        out.print("3.\tQuit\n");
        out.print("4.\tHelp\n");
        String line = scanner.nextLine();
        if (line.equals("1") || line.equals("2")) {
            loggedIn();
        }
        else if (line.equals("4")) {
            initial();
        }
        else {
            out.print(line);
            out.print("NO");
        }
    }
    private void loggedIn() {
        out.print("1.\tCreate game\n");
        out.print("2.\tList games\n");
        out.print("3.\tJoin game\n");
        out.print("4.\tObserve game\n");
        out.print("5.\tLogout\n");
        out.print("6.\tQuit\n");
        out.print("7.\tHelp\n");
        String line = scanner.nextLine();
        if (line.equals("5")) {
            initial();
        }
        else if (line.equals("6")) {
            out.print("Done");
        }
        else if (line.equals("7")) {
            loggedIn();
        }
        else if (line.equals("3")) {
            boardDraw.drawBoth();
        }
        else {
            out.print("Heyo");
        }
    }
}
