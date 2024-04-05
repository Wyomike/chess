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
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.xml.crypto.Data;
import java.io.IOException;

@WebSocket
public class WebsocketCommunicator {

    SessionHandler sessionHandler = new SessionHandler();

    //Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

//    @OnOpen
//    public void open(javax.websocket.Session session) {
//        System.out.println("OPENING");
//        this.session = session;
//    }

//    @OnOpen
//    public void run() { //replace this with a run class, call from client main.
//        //Possibly want to make a unique class of ServerMessageObserver that acts like a message handler
//        Spark.port(8080);//probably want a port parameter!
//        Spark.webSocket("/connect", WebsocketCommunicator.class); //check on class here.
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//
//        try {
////            String testurl = "http://localhost:8080/connect";
////            URL url = new URL(testurl);
////            ContainerProvider.getWebSocketContainer().connectToServer(this, url.toURI());
//            URI uri = new URI("ws://localhost:8080/connect");
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            this.session = container.connectToServer(this, uri);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//
//        UserGameCommand message = new JoinPlayer("a", 1234, ChessGame.TeamColor.WHITE);
//        String json = new Gson().toJson(message, UserGameCommand.class);
//        try {
//            session.getBasicRemote().sendText(json);
//        }
//        catch (Exception e) {//this is a dummy catch
//            System.out.println(e.getMessage());
//        }
//    }

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
                makeMove(move, session);
                break;
            }
            case UserGameCommand.CommandType.RESIGN: {
                resign();
                break;
            }
            case UserGameCommand.CommandType.LEAVE: {
                leave();
                break;
            }
            default: {
                System.out.print("How did you get here?");
            }
        }
        System.out.printf("Received: %s", message);
//        try {
//            //session.getRemote().sendString(message);
//        }
//        catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }


    }

    private void joinPlayer(JoinPlayer command, Session session) throws IOException {
        System.out.println("JOIN a player");
        String authToken = command.getAuthString();
        sessionHandler.add(authToken, session);
        if (!verifyAuth(authToken)) {
            sessionHandler.sendError(authToken, "Invalid authtoken");
            return;
        }
        String color;
        try {
            if (!validJoin(command.getPlayerColor(), command.getGameID(), authToken)) {
                sessionHandler.sendError(authToken, "Color already taken or not reserved");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, accessException.getMessage());
            return;
        }

        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            color = "WHITE";
        }
        else {
            color = "BLACK";
        }//used in notification
        String notify = String.format("%s has joined as %s", command.getUsername(), color);
        LoadGame loadGame = new LoadGame(command.getGameID());
        Notification notification = new Notification(notify);
        sessionHandler.sendMessage(authToken, loadGame);
        sessionHandler.sendNotification(authToken, notification);

    }
    private void joinObserver(JoinObserver command, Session session) throws IOException {
        System.out.println("JOIN observer");
        String authToken = command.getAuthString();
        sessionHandler.add(authToken, session);
        if (!verifyAuth(authToken)) {
            sessionHandler.sendError(authToken, "Invalid authtoken");
            return;
        }
        try {
            if (!validGame(command.getGameID())) {
                sessionHandler.sendError(authToken, "Error retrieving game");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, accessException.getMessage());
            return;
        }
        String notify = String.format("%s has joined as an observer", command.getUsername());
        Notification notification = new Notification(notify);
        LoadGame loadGame = new LoadGame(command.getGameID());
        sessionHandler.sendMessage(authToken, loadGame);
        sessionHandler.sendNotification(authToken, notification);
    }

    private void makeMove(MakeMove command, Session session) throws IOException {//still have to execute move
        System.out.println("MAKE MOVE");//TODO - apply move
        String authToken = command.getAuthString();
        sessionHandler.add(authToken, session);
        ChessMove move = command.getMove();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        try {
            if (!validMove(authToken, command.getGameID(), move)) {//here handle invalid gameID
                sessionHandler.sendError(authToken, "Invalid move");
                return;
            }
        }
        catch (DataAccessException accessException) {
            sessionHandler.sendError(authToken, accessException.getMessage());
            return;
        }
        String notify = String.format("%s has moved the piece at %d,%d to %d,%d", command.getUsername(),
                start.getColumn(), start.getRow(), end.getColumn(), end.getRow());
        Notification notification = new Notification(notify);
        LoadGame loadGame = new LoadGame(command.getGameID());
        sessionHandler.sendMessageToAll(authToken, loadGame);
        sessionHandler.sendNotification(authToken, notification);
    }

    private void resign() {
        //TODO
    }
    private void leave() {

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
}
