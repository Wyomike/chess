package ui;

import Server.ResponseException;
import Server.ServerFacade;
import chess.ChessBoard;
import model.GameData;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

//import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_WHITE;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class Menu {//This is client? maybe I should refactor it to that.
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private Scanner scanner = new Scanner(System.in);
    private ChessBoardDraw boardDraw;
    private ServerFacade facade;
    ArrayList<GameData> games = null;

    String authToken = null;

    public Menu(ChessBoard chess, int url) {
        boardDraw = new ChessBoardDraw(chess);
        facade = new ServerFacade(url);
    }

    public void run() {
        initial();
    }

    public void initial() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("Enter the number of any menu option\n");
        out.print("1.\tRegister\n");
        out.print("2.\tLogin\n");
        out.print("3.\tQuit\n");
        out.print("4.\tHelp\n");
        String line = scanner.nextLine();
        switch (line) {
            case "1" -> register();
            case "2" -> login();
            case "3" -> out.print("Done");
            case "4" -> helpInitial();
            default -> {
                out.print("Please enter a valid number from 1 to 4");
                initial();
            }
        }
    }
    private void loggedIn() {
        out.print("Enter the number of any menu option\n");
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
            case "3" -> joinGame();
            case "4" -> watchGame();
            case "5" -> logout();
            case "6" -> out.print("Done");
            case "7" -> helpLoggedIn();
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
        try {
            out.println("Games:\n");
            games = facade.listGames(authToken);
            for (int i = 0; i < games.size(); ++i) {
                GameData game = games.get(i);
                out.print(i);
                out.print(": ");
                out.print("Game name - ");
                out.print(game.gameName());
                out.print("   ID - ");
                out.print(game.gameID());
                out.print("   White - ");
                out.print(game.whiteUsername());
                out.print("   Black - ");
                out.print(game.blackUsername());
                out.print("\n");
            }
            //out.println(facade.listGames(authToken));
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
            //out.print("Your auth token: ");
            authToken = facade.login(username, password).authToken();
            //out.println(authToken);
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

    private void helpInitial() {
        out.print("1.\tRegister - Register a new user\n");
        out.print("2.\tLogin - Login an existing user\n");
        out.print("3.\tQuit - Exit program\n");
        out.print("4.\tHelp - Display available commands\n");
        initial();
    }
    private void helpLoggedIn() {
        out.print("1.\tCreate game - Create a game with a given name\n");
        out.print("2.\tList games - List all games\n");
        out.print("3.\tJoin game - Join a game as a player\n");
        out.print("4.\tObserve game - Watch a game\n");
        out.print("5.\tLogout - Log out of current session\n");
        out.print("6.\tQuit - Exit program\n");
        out.print("7.\tHelp - Display available commands\n");
        loggedIn();
    }

    private void joinGame() {
        try {
            out.println("Games:\n");
            games = facade.listGames(authToken);
            for (int i = 0; i < games.size(); ++i) {
                GameData game = games.get(i);
                out.print(i);
                out.print(": ");
                out.print("Game name - ");
                out.print(game.gameName());
                out.print("   ID - ");
                out.print(game.gameID());
                out.print("   White - ");
                out.print(game.whiteUsername());
                out.print("   Black - ");
                out.print(game.blackUsername());
                out.print("\n");
            }
            out.print("Enter the number of the game you'd like to join and the color you'd like to join as (WHITE or BLACK)\n");
            int gameID = Integer.parseInt(scanner.next());
            String color = scanner.next();
            scanner.nextLine();
            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                out.println("Invalid color");
                loggedIn();
            }
            if (gameID < 0 || gameID > games.size()) {
                out.println("Invalid index");
                loggedIn();
            }
            facade.joinGame(color, games.get(gameID).gameID(), authToken);
            boardDraw.drawBoth();
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
            loggedIn();
        }
    }
    private void watchGame() {
        try {
            out.println("Games:\n");
            games = facade.listGames(authToken);
            for (int i = 0; i < games.size(); ++i) {
                GameData game = games.get(i);
                out.print(i);
                out.print(": ");
                out.print("Game name - ");
                out.print(game.gameName());
                out.print("   ID - ");
                out.print(game.gameID());
                out.print("   White - ");
                out.print(game.whiteUsername());
                out.print("   Black - ");
                out.print(game.blackUsername());
                out.print("\n");
            }
            out.print("Enter the number of the game you'd like to watch\n");
            int gameID = Integer.parseInt(scanner.next());
            scanner.nextLine();
            if (gameID < 0 || gameID > games.size()) {
                out.println("Invalid index");
                loggedIn();
            }
            out.print("Game exists, if TA asks tell them slack says we only draw an example board at this point");
            boardDraw.drawBoth();
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err");
        }
    }
}
