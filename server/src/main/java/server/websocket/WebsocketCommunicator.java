package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebsocketCommunicator {

    SessionHandler sessionHandler = new SessionHandler();

    //So, we will have the run have a few things happen, do some commands like server, then,
    //we'll do certain things when we receive those messages. These honestly might have to access DAOs?
    //or maybe they just call Server commands?
    @OnWebSocketMessage //automatically called when websocket is received.
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class); //may have to make new objects in switch case.
        //ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.JOIN_PLAYER: {
                JoinPlayer join = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(join, session);
                break;
            }
            case UserGameCommand.CommandType.JOIN_OBSERVER: {
                JoinObserver join = new Gson().fromJson(message, JoinObserver.class);
                joinObserver(join, session);
                break;
            }
            case UserGameCommand.CommandType.MAKE_MOVE: {
                MakeMove move = new Gson().fromJson(message, MakeMove.class);
                makeMove(move);
                break;
            }
            case UserGameCommand.CommandType.RESIGN: {
                Resign resign = new Gson().fromJson(message, Resign.class);
                resign(resign);
                break;
            }
            case UserGameCommand.CommandType.LEAVE: {
                Leave leave = new Gson().fromJson(message, Leave.class);
                leave(leave, session);
                break;
            }
            default: {
                System.out.print("How did you get here?");
            }
        }
        System.out.printf("Received: %s \n", message);
    }

    private void joinPlayer(JoinPlayer command, Session session) throws IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        sessionHandler.add(authToken, gameID, session);
        if (!verifyAuth(authToken)) {
            sessionHandler.sendError(authToken, gameID, "Invalid authtoken");
            return;
        }
        String color;
        try {
            if (!validJoin(command.getPlayerColor(), command.getGameID(), authToken)) {
                sessionHandler.sendError(authToken, gameID, "Color already taken or not reserved");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, gameID, accessException.getMessage());
            return;
        }

        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            color = "WHITE";
        }
        else {
            color = "BLACK";
        }//used in notification
        String notify = String.format("%s has joined as %s", command.getUsername(), color);
        System.out.println("\n sending notification");
        try {
            LoadGame loadGame = new LoadGame(new SQLGameDAO().getGame(gameID), gameID);
            Notification notification = new Notification(notify);
            sessionHandler.sendMessage(authToken, gameID, loadGame);
            sessionHandler.sendNotification(authToken, gameID, notification);
        }
        catch (DataAccessException accessException) {
            System.out.println("Error fetching game: " + accessException.getMessage());
        }
    }
    private void joinObserver(JoinObserver command, Session session) throws IOException {
        int gameID = command.getGameID();
        System.out.println("JOIN observer");
        String authToken = command.getAuthString();
        sessionHandler.add(authToken, gameID, session);
        if (!verifyAuth(authToken)) {
            sessionHandler.sendError(authToken, gameID, "Invalid authtoken");
            return;
        }
        try {
            if (!validGame(command.getGameID())) {
                sessionHandler.sendError(authToken, gameID, "Error retrieving game");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, gameID, accessException.getMessage());
            return;
        }
        String notify = String.format("%s has joined as an observer", command.getUsername());
        try {
            Notification notification = new Notification(notify);
            LoadGame loadGame = new LoadGame(new SQLGameDAO().getGame(gameID), gameID);
            sessionHandler.sendMessage(authToken, gameID, loadGame);
            sessionHandler.sendNotification(authToken, gameID, notification);
            System.out.println("have sent notify observe");
        }
        catch (DataAccessException accessException) {
            System.out.println("Error fetching game: " + accessException.getMessage());
        }
    }

    private void makeMove(MakeMove command) throws IOException {//still have to execute move
        int gameID = command.getGameID();
        System.out.println("MAKE MOVE");
        String authToken = command.getAuthString();
        ChessMove move = command.getMove();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        try {
            if (!validMove(authToken, command.getGameID(), move)) {//here handle invalid gameID
                sessionHandler.sendError(authToken, gameID, "Invalid move");
                System.out.println("\nreturned false");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, gameID, accessException.getMessage());
            System.out.println("\nreturned error");
            return;
        }
        Notification notification = getNotification(command, start, end);
        try {
            LoadGame loadGame = new LoadGame(new SQLGameDAO().getGame(gameID), gameID);
            sessionHandler.sendMessageToAll(authToken, gameID, loadGame);
            sessionHandler.sendNotification(authToken, gameID, notification);
        }
        catch (DataAccessException accessException) {
            System.out.println("Error fetching game: " + accessException.getMessage());
        }
    }

    private static Notification getNotification(MakeMove command, ChessPosition start, ChessPosition end) {
        String[] chessLetters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        String startCol = "";
        String endCol = "";
        for (int i = 0; i < 8; ++i) {
            if (start.getColumn() == i) startCol = chessLetters[i - 1];
            if (end.getColumn() == i) endCol = chessLetters[i - 1];
        }
        String notify = String.format("%s has moved the piece at %s,%d to %s,%d", command.getUsername(),
                startCol, start.getRow(), endCol, end.getRow());
        Notification notification = new Notification(notify);
        return notification;
    }

    private void resign(Resign resign) {
        int gameID = resign.getGameID();
        try {
            if (!isPlayer(resign.getAuthString(), resign.getGameID())) {
                String error = "You are not a player!";
                sessionHandler.sendError(resign.getAuthString(), gameID, error);
            }
            else if (new SQLGameDAO().getGame(resign.getGameID()).game().isDone()) {
                String error = "Game is already finished";
                sessionHandler.sendError(resign.getAuthString(), gameID, error);
            }
            else {
                SQLGameDAO gameDAO = new SQLGameDAO();
                ChessGame game = gameDAO.getGame(resign.getGameID()).game();
                game.setDone();
                gameDAO.updateGame(resign.getGameID(), game);
                //notify here
                String notify = String.format("%s has resigned", resign.getUsername());
                Notification notification = new Notification(notify);
                sessionHandler.sendMessageToAll(resign.getAuthString(), gameID, notification);
            }
        }
        catch (DataAccessException | IOException exception) {
            System.out.println(exception.getMessage());//fix?
        }
    }
    private void leave(Leave leave, Session session) throws IOException {
        int gameID = leave.getGameID();
        String notify = String.format("%s has left the game", leave.getUsername());
        try {
            new SQLGameDAO().leavePlayer(leave.getUsername(), leave.getGameID());
            Notification notification = new Notification(notify);
            sessionHandler.sendNotification(leave.getAuthString(), gameID, notification);
            System.out.println("Such and such has left the game");
            sessionHandler.remove(leave.getAuthString(), gameID);
        }
        catch (DataAccessException accessException) {
            System.out.println(accessException.getMessage());
        }
    }

    private boolean validGame(int gameID) throws DataAccessException{
        GameData check = new SQLGameDAO().getGame(gameID);
        if (check == null) {
            throw new DataAccessException("Failed to retrieve game: invalid ID");
        }
        return true;
    }
    private boolean validJoin(ChessGame.TeamColor color, int gameID, String authToken) throws DataAccessException {
        try {
            String reserved = new SQLAuthDAO().getAuth(authToken).username();
            GameData check = new SQLGameDAO().getGame(gameID);
            if (check == null) {
                throw new DataAccessException("Failed to retrieve game: invalid ID");
            }
            if (color == ChessGame.TeamColor.WHITE) {
                if (check.whiteUsername() == null) {
                    return false;
                }
                else if (check.whiteUsername().equals(reserved)) {
                    return true;
                }
            }
            else if (color == ChessGame.TeamColor.BLACK){
                if (check.blackUsername() == null) {
                    return false;
                }
                if (check.blackUsername().equals(reserved)) {
                    return true;
                }
            }
            else {
                return false;
            }
        }
        catch (DataAccessException accessException) {
            //throw new DataAccessException("Failed to retrieve game: invalid ID");
            System.out.println("Failed to retrieve game: ");
            System.out.println(accessException.getMessage());
        }
        return false;
    }
    private boolean verifyAuth(String auth) {
        try {
            return new SQLAuthDAO().getAuth(auth) != null;
        }
        catch (DataAccessException accessException) {
            return false;
        }
    }
    private boolean validMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        String player = new SQLAuthDAO().getAuth(authToken).username();
        if (validGame(gameID)) {
            SQLGameDAO gameAccess = new SQLGameDAO();
            GameData gameData = gameAccess.getGame(gameID);

            ChessGame game = gameData.game();
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(move.getStartPosition()).getTeamColor();
            ChessGame.TeamColor turnColor = game.getTeamTurn();
            if (turnColor != pieceColor) {
                return false;//wrong turn
            }
            if (!game.validMoves(move.getStartPosition()).contains(move)) {//check if valid moves has move
                return false; //invalid move
            }
            if (!playerIsMoveOwner(gameData, player, pieceColor)) {
                return false;
            }
            if (game.isInStalemate(turnColor) || game.isInCheckmate(turnColor)) {
                return false;
            }
            try {
                game.makeMove(move);
                gameAccess.updateGame(gameID, game);
            }
            catch (InvalidMoveException invalidMoveException) {
                throw new DataAccessException(invalidMoveException.getMessage());
            }
        }
        else {
            return false;
        }

        return true;
    }
    private boolean playerIsMoveOwner(GameData game, String player, ChessGame.TeamColor pieceColor) {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername().equals(player)) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername().equals(player)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    private boolean isPlayer(String authToken, int gameID) {
        try {
            String username = new SQLAuthDAO().getAuth(authToken).username();
            GameData gameData = new SQLGameDAO().getGame(gameID);
            return username.equals(gameData.blackUsername()) || username.equals(gameData.whiteUsername());
        }
        catch (DataAccessException accessException) {
            System.out.println(accessException.getMessage());
            return false;
        }
    }
}
