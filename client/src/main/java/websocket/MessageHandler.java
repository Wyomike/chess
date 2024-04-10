package websocket;

import com.google.gson.Gson;
import model.GameData;
import server.ServerFacade;
import ui.Menu;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;

public class MessageHandler {
    ServerFacade facade;
    String authToken;
    GameData game;
    Menu menu;

    public MessageHandler(ServerFacade facade, String authToken, Menu menu) {
        this.facade = facade;
        this.authToken = authToken;
        this.menu = menu;
    }
    public void error(String message) {
        webSocketMessages.serverMessages.Error error = new Gson().fromJson(message, webSocketMessages.serverMessages.Error.class);
        System.out.println("ERROR: " + error.getErrorMessage());
    }
    public void notify(String message) {
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println("Notification: " + notification.getMessage());
    }
    public void loadGame(LoadGame load) throws IOException, server.ResponseException {
        game = load.getGame();
        menu.setGame(game);
        menu.drawBoard();
    }

    public GameData getGame() {
        return game;
    }
}
