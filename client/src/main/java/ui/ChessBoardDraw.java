package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardDraw {
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    ChessPiece[][] board;
    public ChessBoardDraw(ChessBoard board) {
        this.board = board.getBoard();
    }

    public void drawBoard() {
        setLightGray();
        out.print("    a   b   c   d   e   f   g   h     \n");
        for (int i = 0; i < 8; ++i) {
            setLightGray();
            out.print(" ");
            out.print(i + 1);
            out.print(" ");
            for (int j = 0; j < 8; ++j) {
                drawSquare(i,j);
            }
            setLightGray();
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" ");
            out.print(i + 1);
            out.print(" ");
            out.print("\n");
        }

        out.print("    a   b   c   d   e   f   g   h     \n");
    }

    private void drawSquare(int i, int j) {
        ChessPiece piece = board[i][j];
        if ((i + j) % 2 == 0) setWhite();
        else setBlack();
        out.print(" ");
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) out.print(SET_TEXT_COLOR_LIGHT_GREY);
            else out.print(SET_TEXT_COLOR_BLUE);
            out.print(piece.toString());
        }
        else out.print("  ");
        out.print(" ");//break this out into a printSquare(i,j) function
    }

    private void setBlack() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private void setWhite() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private void setLightGray() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
