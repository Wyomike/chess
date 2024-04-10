package server;

import model.*;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ServerFacade {
    private final String serverUrl;
    private final HttpHandler httpHandler;

    public ServerFacade(int url) {
        String urlHead = "http://localhost:";
        urlHead += String.valueOf(url);
        serverUrl = urlHead;
        httpHandler = new HttpHandler(serverUrl);
    }
    public AuthData register(String username, String password, String email) throws ResponseException, IOException {
        UserData userData = new UserData(username, password, email);
        return httpHandler.register(userData);
    }

    public AuthData login(String username, String password) throws ResponseException, IOException {
        LoginRequest login = new LoginRequest(username, password);
        return httpHandler.login(login);
    }

    public void logout(String authToken) throws ResponseException, IOException {
        httpHandler.logout(authToken);
    }

    public ArrayList<GameData> listGames(String authToken) throws ResponseException, IOException {
         ArrayList<GameData> games = httpHandler.listGames(authToken);
        return games;
    }

    public GameData createGame(String gameName, String authToken) throws ResponseException, IOException {
        return httpHandler.createGame(gameName, authToken);
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws ResponseException, IOException {
        httpHandler.joinGame(playerColor, gameID, authToken);
    }

    public void clear() throws ResponseException {
        try {
            httpHandler.clear();
        }
        catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }
    public String getServerUrl() {
        return serverUrl;
    }
}
