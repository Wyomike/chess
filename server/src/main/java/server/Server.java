package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;

public class Server {

    private final ClearHandler clearHandler = new ClearHandler();
    private final GameHandler gameHandler = new GameHandler();
    private final UserHandler userHandler = new UserHandler();

    public Server() {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        return clearHandler.clearServer(request,response);
    }
    private Object register(Request request, Response response) {
        return userHandler.register(request,response);
    }
    private Object login(Request request, Response response) {
        return userHandler.login(request,response);
    }
    private Object logout(Request request, Response response) {
        return userHandler.logout(request,response);
    }
    private Object listGames(Request request, Response response) {
        return gameHandler.listGames(request,response);
    }
    private Object createGame(Request request, Response response) {
        return gameHandler.createGame(request,response);
    }
    private Object joinGame(Request request, Response response) {
        return gameHandler.joinGame(request,response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}