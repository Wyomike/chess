package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {
    ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>(); //map of authToken string to session

    public void add(String authToken, Session session) {
        sessions.put(authToken, session);
    }
    public void remove(String authToken) {
        sessions.remove(authToken);
    }

    public void sendMessage(String authToken, ServerMessage message) throws IOException {
        String toSend = new Gson().toJson(message);
        sessions.get(authToken).getRemote().sendString(toSend);
    }
    public void sendNotification(String excludeVisitorName, ServerMessage message) throws IOException {
        if (sessions.size() > 1) {
            var removeList = new ArrayList<String>();
            for (var c : sessions.entrySet()) {
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
                sessions.remove(c);
            }
        }
    }
}
