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

    public void drawBoth() {
        drawBoard();
        setBlack();
        out.println();
        drawBoardReversed();
    }
    public void drawBoard() {
        drawHeader(false);
        for (int i = 0; i < 8; ++i) {
            edgeNumber(i, false);
            for (int j = 0; j < 8; ++j) {
                drawSquare(i,j);
            }
            edgeNumber(i, false);
            setBlack();
            out.print("\n");
        }
        drawHeader(false);
    }

    public void drawBoardReversed() {
        drawHeader(true);
        for (int i = 0; i < 8; ++i) {
            edgeNumber(i, true);
            for (int j = 0; j < 8; ++j) {
                drawSquare(7 - i,7 - j);
            }
            edgeNumber(i, true);
            setBlack();
            out.print("\n");
        }
        drawHeader(true);
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
    private void drawHeader(boolean reversed) {
        if (!reversed) {
            setLightGray();
            out.print("    a   b   c   d   e   f   g   h     ");
            setBlack();
            out.print("\n");
        }
        else {
            setLightGray();
            out.print("    h   g   f   e   d   c   b   a     ");
            setBlack();
            out.print("\n");
        }
    }
    private void edgeNumber(int i, boolean reversed) {
        if (!reversed) {
            setLightGray();
            out.print(" ");
            out.print(i + 1);
            out.print(" ");
        }
        else {
            setLightGray();
            out.print(" ");
            out.print(8 - i);
            out.print(" ");
        }
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
