package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;

public class Server {

    private final ClearHandler clearHandler = new ClearHandler();
    private final RegisterHandler registerHandler = new RegisterHandler();
    private final LoginHandler loginHandler = new LoginHandler();
    private final LogoutHandler logoutHandler = new LogoutHandler();
    private final GameHandler gameHandler = new GameHandler();

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
        return registerHandler.register(request,response);
    }
    private Object login(Request request, Response response) {
        return loginHandler.login(request,response);
    }
    private Object logout(Request request, Response response) {
        return logoutHandler.logout(request,response);
    }
    private Object listGames(Request request, Response response) {
        return loginHandler.login(request,response);
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