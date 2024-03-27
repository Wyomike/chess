package Server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WebsocketCommunicator {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WebsocketCommunicator.class); //check on class here.
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }
}
