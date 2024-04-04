package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

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

        System.out.println("HEYO");
        System.out.println("HEYO");
        System.out.println("HEYO");

        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.JOIN_PLAYER: {
                JoinPlayer join = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(join, session);
                break;
            }
            case UserGameCommand.CommandType.JOIN_OBSERVER: {
                JoinObserver join = new Gson().fromJson(message, JoinObserver.class);
                //joinObserver(join, session);
                joinObserver(join, session);
                break;
            }
            case UserGameCommand.CommandType.MAKE_MOVE: {
                makeMove();
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
        //JoinPlayer join = new JoinPlayer(command);
        sessionHandler.add(command.getAuthString(), session);
        String color;
        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            color = "WHITE";
        }
        else {
            color = "BLACK";
        }//idk what to do with this
        String notify = String.format("%s has joined as %s", command.getUsername(), color);
        LoadGame loadGame = new LoadGame(command.getGameID());
        Notification notification = new Notification(notify);
        sessionHandler.sendMessage(command.getAuthString(), loadGame);
        sessionHandler.sendNotification(command.getAuthString(), notification);
    }
    private void joinObserver(JoinObserver command, Session session) throws IOException {
        System.out.println("JOIN observer");
        //JoinPlayer join = new JoinObserver(command);
        sessionHandler.add(command.getAuthString(), session);
        String notify = String.format("%s has joined as an observer", command.getUsername());
        LoadGame loadGame = new LoadGame(command.getGameID());
        sessionHandler.sendMessage(command.getUsername(), loadGame);
    }
    private void makeMove() {
        //TODO
    }
    private void resign() {
        //TODO
    }
    private void leave() {

    }
}
