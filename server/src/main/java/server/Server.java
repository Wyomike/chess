package server;

import dataAccess.*;
import spark.*;

public class Server {

    private AuthDAO authDAO = new MemoryAuthDAO();
    private UserDAO userDAO = new SQLUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();

    private final ClearHandler clearHandler = new ClearHandler(authDAO, userDAO, gameDAO);
    private final GameHandler gameHandler = new GameHandler(authDAO, gameDAO);
    private final UserHandler userHandler = new UserHandler(authDAO, userDAO);

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
        System.out.println("CLEAR");
        return clearHandler.clearServer(request,response);
    }
    private Object register(Request request, Response response) {
        System.out.println("REGISTER");
        return userHandler.register(request,response);
    }
    private Object login(Request request, Response response) {
        System.out.println("LOGIN");
        return userHandler.login(request,response);
    }
    private Object logout(Request request, Response response) {
        System.out.println("LOGOUT");
        return userHandler.logout(request,response);
    }
    private Object listGames(Request request, Response response) {
        System.out.println("LISTGAME");
        return gameHandler.listGames(request,response);
    }
    private Object createGame(Request request, Response response) {
        System.out.println("CREATEGAME");
        return gameHandler.createGame(request,response);
    }
    private Object joinGame(Request request, Response response) {
        System.out.println("JOINGAME");
        return gameHandler.joinGame(request,response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}