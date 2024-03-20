package ui;

import Server.ResponseException;
import Server.ServerFacade;
import chess.ChessBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Menu {//This is client? maybe I should refactor it to that.
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private Scanner scanner = new Scanner(System.in);
    private ChessBoardDraw boardDraw;
    private ServerFacade facade;

    String authToken = null;

    public Menu(ChessBoard chess, int url) {
        boardDraw = new ChessBoardDraw(chess);
        facade = new ServerFacade(url);
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
        switch (line) {
            case "1" -> register();
            case "2" -> login();
            case "3" -> out.print("Done");
            case "4" -> initial();
            default -> {
                out.print("Please enter a valid number from 1 to 4");
                initial();
            }
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
        switch (line) {
            case "1" -> createGame();
            case "2" -> listGames();
            case "3" -> boardDraw.drawBoth();
            case "4" -> boardDraw.drawBoard();
            case "5" -> logout();
            case "6" -> out.print("Done");
            case "7" -> loggedIn();
            default -> {
                out.println("Please enter a number from 1 to 7");
                loggedIn();
            }
        }
    }

    private void createGame() {
        out.println("Please enter a name for the game.");
        String gameName = scanner.next();
        //String authToken = scanner.next();
        scanner.nextLine();
        try {
            facade.createGame(gameName, authToken);
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }
    private void listGames() {
        //out.println("Please enter an auth token.");
        //String authToken = scanner.next();
        //scanner.nextLine();
        try {
            out.println(facade.listGames(authToken));
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }

    private void register() {
        out.println("Please enter a username, password, and email separated by spaces");
        String username = scanner.next();
        String password = scanner.next();
        String email = scanner.next();
        scanner.nextLine();
        try {
            //out.print("Your auth token: ");
            authToken = facade.register(username, password, email).authToken();
            //out.println(authToken);
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }
    private void login() {
        out.println("Please enter a valid username and password separated by spaces");
        String username = scanner.next();
        String password = scanner.next();
        scanner.nextLine();
        try {
            out.print("Your auth token: ");
            authToken = facade.login(username, password).authToken();
            out.println(authToken);
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }
    private void logout() {
        try {
            facade.logout(authToken);
            authToken = null;
            initial();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }
}
