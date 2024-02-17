package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;

public class Server {

    private final ClearHandler clearHandler;

    public Server(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        clearHandler = new ClearHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {

        return clearHandler.clearServer(request,response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}