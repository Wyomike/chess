package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardDraw {
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private ChessPiece[][] board;
    public ChessBoardDraw(ChessBoard board) {
        this.board = board.getBoard();
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
        out.print(SET_BG_COLOR_WHITE);
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
        out.print(SET_BG_COLOR_WHITE);
    }

    public void highlightMoves(boolean[][] validSpaces, int row, int col) {
        drawHeader(false);
        for (int i = 0; i < 8; ++i) {
            edgeNumber(i, false);
            for (int j = 0; j < 8; ++j) {
                if (validSpaces[i][j]) {
                    highlightSquare(i,j);
                }
                else if (i == row - 1 && j == col -1) {
                    drawYellow(i,j);
                }
                else {
                    drawSquare(i,j);
                }
            }
            edgeNumber(i, false);
            setBlack();
            out.print("\n");
        }
        drawHeader(false);
        out.print(SET_BG_COLOR_WHITE);
    }

    private void drawSquare(int i, int j) {
        ChessPiece piece = board[i][j];
        if ((i + j) % 2 == 0) setWhite();
        else setBlack();
        fillSquare(piece);
    }
    private void highlightSquare(int i, int j) {
        ChessPiece piece = board[i][j];
        if ((i + j) % 2 == 0) setLightGreen();
        else setGreen();
        fillSquare(piece);
    }
    private void drawYellow(int i, int j) {
        ChessPiece piece = board[i][j];
        setYellow();
        fillSquare(piece);
    }
    private void fillSquare(ChessPiece piece) {
        out.print(" ");
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) out.print(SET_TEXT_COLOR_LIGHT_GREY);
            else out.print(SET_TEXT_COLOR_BLUE);
            out.print(piece.toString());
        }
        else out.print("  ");
        out.print(" ");
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
    private void setLightGreen() {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private void setGreen() {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private void setYellow() {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}
