package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {
    //ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>(); //map of authToken string to session
    ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> sessionsByGame = new ConcurrentHashMap<>();

    public void add(String authToken, int gameID, Session session) {
        if (sessionsByGame.get(gameID) == null) {
            ConcurrentHashMap<String, Session> gameSessions = new ConcurrentHashMap<>();
            gameSessions.put(authToken, session);
            sessionsByGame.put(gameID, gameSessions);
        }
        else {
            sessionsByGame.get(gameID).put(authToken, session);
        }
    }
    public void remove(String authToken, int gameID) {
        sessionsByGame.get(gameID).remove(authToken);
        //sessions.remove(authToken);
    }

    public void sendError(String authToken, int gameID, String message) throws IOException {
        webSocketMessages.serverMessages.Error error = new Error(message);
        String toSend = new Gson().toJson(error);
        sessionsByGame.get(gameID).get(authToken).getRemote().sendString(toSend);
    }
    public void sendMessage(String authToken, int gameID, ServerMessage message) throws IOException {
        String toSend = new Gson().toJson(message);
        sessionsByGame.get(gameID).get(authToken).getRemote().sendString(toSend);
    }
    public void sendMessageToAll(String authToken, int gameID, ServerMessage message) throws IOException {
        String toSend = new Gson().toJson(message);
        if (sessionsByGame.get(gameID).size() > 1) {
            for (var c : sessionsByGame.get(gameID).values()) {
                c.getRemote().sendString(toSend);
            }
        }
    }
    public void sendNotification(String excludeVisitorName, int gameID, ServerMessage message) throws IOException {
        if (sessionsByGame.get(gameID).size() > 1) {
            var removeList = new ArrayList<String>();
            for (var c : sessionsByGame.get(gameID).entrySet()) {
                if (c.getValue().isOpen()) {
                    if (!c.getKey().equals(excludeVisitorName)) {
                        String toSend = new Gson().toJson(message);
                        c.getValue().getRemote().sendString(toSend);
                    }
                } else {
                    removeList.add(c.getKey());
                }
            }

            // Clean up any connections that were left open.
            for (var c : removeList) {
                sessionsByGame.get(gameID).remove(c);
            }
        }
    }
}
