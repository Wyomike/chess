package ui;

import server.ResponseException;
import server.ServerFacade;
import chess.*;
import model.GameData;
import websocket.MessageHandler;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import static ui.EscapeSequences.SET_BG_COLOR_WHITE;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class Menu {
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private Scanner scanner = new Scanner(System.in);
    private ChessBoardDraw boardDraw;
    private ServerFacade facade;
    private WebSocketFacade wsFacade;
    private MessageHandler messageHandler;
    private GameData game;
    ArrayList<GameData> games = null;

    private String authToken = null;
    private String username = null;
    int gameID;

    public Menu(ChessBoard chess, int url) {
        //boardDraw = new ChessBoardDraw(chess);
        facade = new ServerFacade(url);
    }

    public void run() {
        //this websocket new up should go to join and observe eventually
//        new WebsocketCommunicator().run();
        //
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
            out.println("err" + exception.getMessage());
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
                if (game.game().isDone()) {
                    out.print("FINISHED ");
                }
                listDetails(game);
                out.print("\n");
            }
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err" + exception.getMessage());
        }
    }

    private void listDetails(GameData game) {
        out.print("Game name - ");
        out.print(game.gameName());
        out.print("   ID - ");
        out.print(game.gameID());
        out.print("   White - ");
        out.print(game.whiteUsername());
        out.print("   Black - ");
        out.print(game.blackUsername());
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
            this.username = username;
            messageHandler = new MessageHandler(facade, authToken, this);
            //out.println(authToken);
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err" + exception.getMessage());
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
            this.username = username;
            messageHandler = new MessageHandler(facade, authToken, this);
            //out.println(authToken);
            loggedIn();
        }
        catch (IOException | ResponseException exception) {
            out.println("err: " + exception.getMessage());
            initial();
        }
    }
    private void logout() {
        try {
            facade.logout(authToken);
            authToken = null;
            initial();
        }
        catch (IOException | ResponseException exception) {
            out.println("err" + exception.getMessage());
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
                listGames(i);
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
            if (gameID < 0 || gameID > games.size() - 1) {
                out.println("Invalid index");
                loggedIn();
            }
            this.gameID = games.get(gameID).gameID();
            facade.joinGame(color, this.gameID, authToken);
            wsFacade = new WebSocketFacade(facade.getServerUrl(), messageHandler);
            if (color.equals("WHITE")) {
                ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
                wsFacade.joinPlayer(username, authToken, this.gameID, teamColor);
            }
            else {
                ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
                wsFacade.joinPlayer(username, authToken, this.gameID, teamColor);
            }
            game = messageHandler.getGame();
            inGame();
        }
        catch (IOException | ResponseException exception) {
            out.println("err" + exception.getMessage());
            loggedIn();
        }
    }
    private void watchGame() {
        try {
            out.println("Games:\n");
            games = facade.listGames(authToken);
            for (int i = 0; i < games.size(); ++i) {
                listGames(i);
            }
            out.print("Enter the number of the game you'd like to watch\n");
            int gameID = Integer.parseInt(scanner.next());
            scanner.nextLine();
            if (gameID < 0 || gameID > games.size() - 1) {
                out.println("Invalid index");
                loggedIn();
            }
            this.gameID = games.get(gameID).gameID();
            wsFacade = new WebSocketFacade(facade.getServerUrl(), messageHandler);
            wsFacade.joinObserver(username, authToken, this.gameID);
            game = messageHandler.getGame();
            //System.out.print("Done getting game");
            watching();
        }
        catch (IOException | ResponseException exception) {
            out.println("err" + exception.getMessage());
        }
    }

    private void listGames(int i) {
        GameData game = games.get(i);
        out.print(i);
        out.print(": ");
        listDetails(game);
        out.print("   Turn - ");
        out.print(game.game().getTeamTurn());
        out.print("\n");
    }

    private void inGame() {//maybe give an int parameter for which game.
        game = messageHandler.getGame();
        out.println("At any time enter 0 for a help menu");
        out.print("Enter the number of any menu option\n");
        out.print("1.\tRedraw board\n");
        out.print("2.\tMake move\n");
        out.print("3.\tHighlight legal moves\n");
        out.print("4.\tLeave\n");
        out.print("5.\tResign\n");
        out.print("6.\tHelp\n");
        String line = scanner.nextLine();
        switch (line) {
            case "1" -> {
                drawBoard();
                inGame(); //Will have to pass parameter here eventually.
            }
            case "2" -> makeMove();
            case "3" -> {
                highlightMoves();
                inGame();
            }
            case "4" -> leave();
            case "5" -> resign();
            case "6" -> helpGame();
            default -> {
                out.println("Please enter a number from 1 to 6");
                inGame();
            }
        }
    }
    private void helpGame() {
        out.print("1.\tRedraw board - redraw the current board from your perspective\n");
        out.print("2.\tMake move - make a move and advance the game\n");
        out.print("3.\tHighlight legal moves - highlight the legal moves of a piece\n");
        out.print("4.\tLeave - leave a game and leave the spot open for another\n");
        out.print("5.\tResign - forfeit the game\n");
        out.print("6.\tHelp - display this menu\n");
        inGame();
    }
    public void drawBoard() {
        boardDraw = new ChessBoardDraw(game.game().getBoard());
        if (username.equals(game.whiteUsername())) {
            boardDraw.drawBoard();
        }
        else if (username.equals(game.blackUsername())) {
            boardDraw.drawBoardReversed();
        }
        else {
            boardDraw.drawBoard();
        }
        out.print("Turn: ");
        if (game.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
            out.print("WHITE\n");
        }
        else {
            out.print("BLACK\n");
        }
    }

    private void makeMove() {
        try {
            out.println("Enter the position of the piece you'd like to move");
            ChessPosition start = parsePosition();
            out.println("Enter the position to which you'd like to move");
            ChessPosition end = parsePosition();
            ChessPiece.PieceType promotion = checkPromotion(start, end);
            ChessMove move = new ChessMove(start, end, promotion);
            wsFacade.makeMove(username, authToken, gameID, move);
        }
        catch (ResponseException responseException) {
            out.println(responseException.getMessage());
        }
        inGame();
    }
    private void leave() {
        try {
            wsFacade.leave(username, authToken, gameID);
            loggedIn();
        }
        catch (ResponseException responseException) {
            out.println(responseException.getMessage());
        }
        loggedIn();
    }
    private void resign() {
        try {
            wsFacade.resign(username, authToken, gameID);
            loggedIn();
        }
        catch (ResponseException responseException) {
            out.println(responseException.getMessage());
        }
        loggedIn();
    }

    private void watching() {
        game = messageHandler.getGame();
        out.println("At any time enter 0 for a help menu");
        out.print("Enter the number of any menu option\n");
        out.print("1.\tRedraw board\n");
        out.print("2.\tHighlight legal moves\n");
        out.print("3.\tLeave\n");
        out.print("4.\tHelp\n");
        String line = scanner.nextLine();
        switch (line) {
            case "1" -> {
                drawBoard();
                watching();
            }
            case "2" -> {
                highlightMoves();
                watching();
            }
            case "3" -> leave();
            case "4", "0" -> helpWatching();
            default -> {
                out.println("Please enter a number from 1 to 4");
                watching();
            }
        }
    }
    private void helpWatching() {
        out.println("At any time enter 0 for a help menu");
        out.print("1.\tRedraw board - redraw the board from white's perspective\n");
        out.print("2.\tHighlight legal moves - Highlight the legal moves a piece can make\n");
        out.print("3.\tLeave - return to the post-login menu\n");
        out.print("4.\tHelp - display this menu\n");
        watching();
    }

    private void highlightMoves() {
        ChessPosition pos = parsePosition();
        boardDraw.highlightMoves(parseMoves(game.game().validMoves(pos)), pos.getRow(), pos.getColumn());
    }

    private ChessPosition parsePosition() {
        String[] chessLetters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        out.println("Enter a coordinate with a letter a-h then a number 1-8, separated by a space. (i.e a 1, or h 4)");
        String colLetter = scanner.next();
        int row = Integer.parseInt(scanner.next());
        int col = -1;
        for (int i = 0; i < 8; ++i) {
            if (colLetter.equals(chessLetters[i])) col = i + 1;
        }
        if (col == -1 || (row < 1 || row > 8)) {
            out.println("Err: invalid position, please enter a letter a-h and a number 1-8 separated by a space");
            return parsePosition();
        }
        scanner.nextLine();
        return new ChessPosition(row, col);
    }

    private boolean[][] parseMoves(Collection<ChessMove> moves) {
        boolean[][] validSpaces = new boolean[8][8];

        Iterator<ChessMove> moveIter = moves.iterator();
        ArrayList<int[]> movePositions = new ArrayList<int[]>();
        while (moveIter.hasNext()) {
            ChessMove move = moveIter.next();
            movePositions.add(new int[] {move.getEndPosition().getRow(), move.getEndPosition().getColumn()});
        }
        for (int i = 0; i < movePositions.size(); ++i) {
            int x = movePositions.get(i)[0];
            int y = movePositions.get(i)[1];
            if (x > -1 && x < 8 && y > -1 && y < 8) {
                validSpaces[x - 1][y - 1] = true;
            }
        }
        return validSpaces;
    }

    private ChessPiece.PieceType checkPromotion(ChessPosition start, ChessPosition end) {
        ChessPiece startPiece = game.game().getBoard().getPiece(start);
        if (startPiece == null) {
            return null;
        }
        if ((startPiece.getTeamColor() == ChessGame.TeamColor.WHITE && start.getRow() == 7) || (startPiece.getTeamColor() == ChessGame.TeamColor.BLACK && start.getRow() == 1)) {
            out.println("Enter a piece type to promote to (cannot be pawn or king), such as QUEEN, or KNIGHT");
            String line = scanner.nextLine();
            switch (line) {
                case ("QUEEN"): return ChessPiece.PieceType.QUEEN;
                case ("BISHOP"): return ChessPiece.PieceType.BISHOP;
                case ("ROOK"): return ChessPiece.PieceType.ROOK;
                case ("KNIGHT"): return ChessPiece.PieceType.KNIGHT;
                default: {
                    out.println("Err: please enter a valid piece type (QUEEN, BISHOP, ROOK, or KNIGHT");
                    return checkPromotion(start, end);
                }
            }
        }
        return null;
    }

    public void setGame(GameData game) {
        this.game = game;
    }
}
