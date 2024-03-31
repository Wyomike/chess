package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.LoginRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.ContainerProvider;
import javax.websocket.OnOpen;
import javax.websocket.*;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

@WebSocket
public class WebsocketCommunicator {
    javax.websocket.Session session;

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
    public void onMessage(javax.websocket.Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer();
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver();
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove();
            case UserGameCommand.CommandType.RESIGN -> resign();
            case UserGameCommand.CommandType.LEAVE -> leave();
        }
        System.out.printf("Received: %s", message);
        session.getBasicRemote().sendText("WebSocket response: " + message);

    }
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) throws IOException {
//        Action action = new Gson().fromJson(message, Action.class);
//        switch (action.type()) {
//            case ENTER -> enter(action.visitorName(), session);
//            case EXIT -> exit(action.visitorName());
//        }
//    }

    private void joinPlayer() {
        System.out.println("JOIN a player");
    }
    private void joinObserver() {
        //TODO
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
