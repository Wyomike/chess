package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.*;
import service.ClearService;
import service.GameService;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler {

    private GameService service;

    public GameHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        service = new GameService(authDAO, gameDAO, userDAO);
    }

    public Object listGames(Request req, Response res) {//here do json stuff.
        String authToken = req.headers("authorization");
        try {
            res.type("application/json");
            var list = service.listGames(authToken).toArray();
            res.status(200);
            return new Gson().toJson(Map.of("games", list));
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
    public Object createGame(Request req, Response res) {//probably need a dummy class here to pass on for game construction
        String authToken = req.headers("authorization");
        try {
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            GameData resultGame = service.createGame(game, authToken);
            res.status(200);
            return new Gson().toJson(new GameID(resultGame.gameID()));
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
    public Object joinGame(Request req, Response res) {
        //Here you're gonna want to make a custom object to get the data, then pass it on from the object
        String authToken = req.headers("authorization");

        try {
            JoinRequest request = new Gson().fromJson(req.body(), JoinRequest.class);
            service.joinGame(request.playerColor(), request.gameID(), authToken);
            res.status(200);
            //String authToken = new Gson().fromJson(req.body(), String.class);
            return new Gson().toJson(null);
        }
        catch (BadRequestException requestException) {
            res.status(400);
            return new Gson().toJson(Map.of("message", requestException.getMessage()));
        }
        catch (ColorException colorException) {
            res.status(403);
            return new Gson().toJson(Map.of("message", colorException.getMessage()));
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
}

