package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import server.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

public class WebSocketFacade extends Endpoint {

    Session session;
    MessageHandler messageHandler;

    public WebSocketFacade(String url, MessageHandler messageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ServerMessage.ServerMessageType.ERROR: {
                            messageHandler.error(message);
                            break;
                        }
                        case ServerMessage.ServerMessageType.NOTIFICATION: {
                            messageHandler.notify(message);
                            break;
                        }
                        case ServerMessage.ServerMessageType.LOAD_GAME: {
                            try {
                                LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                                messageHandler.loadGame(loadGame);
                            }
                            catch (IOException | ResponseException err) {
                                System.out.println("Err loading" + err.getMessage());
                            }
                            break;
                        }
                        default: {
                            System.out.print("How did you get here?");
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String player, String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            JoinPlayer joinPlayer = new JoinPlayer(player, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
        } catch (IOException ex) {
            throw new ResponseException("500: " + ex.getMessage());
        }
    }
    public void joinObserver(String player, String authToken, int gameID) throws ResponseException {
        try {
            JoinObserver joinObserver = new JoinObserver(player, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
        } catch (IOException ex) {
            throw new ResponseException("500: " + ex.getMessage());
        }
    }

    public void makeMove(String player, String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            MakeMove makeMove = new MakeMove(player, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
        } catch (IOException ex) {
            throw new ResponseException("500: " + ex.getMessage());
        }
    }

    public void leave(String player, String authToken, int gameID) throws ResponseException {
        try {
            Leave leave = new Leave(player, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leave));
        } catch (IOException ex) {
            throw new ResponseException("500: " + ex.getMessage());
        }
    }

    public void resign(String player, String authToken, int gameID) throws ResponseException {
        try {
            Resign resign = new Resign(player, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(resign));
        } catch (IOException ex) {
            throw new ResponseException("500: " + ex.getMessage());
        }
    }
}
