package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Menu {
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    Scanner scanner = new Scanner(System.in);
    ChessBoardDraw boardDraw;

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
        if (line.contains("1")) {
            boardDraw.drawBoth();
        }
        else {
            out.print("NO");
        }
    }
}
