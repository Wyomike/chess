package server;

import com.google.gson.Gson;
import model.GameData;
import model.JoinRequest;
import model.UserData;
import service.GameService;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler {

    private GameService service;

    public GameHandler() {
    }

    public Object listGames(Request req, Response res) {//here do json stuff.
        res.type("application/json");
        var list = service.listGames().toArray();
        return new Gson().toJson(Map.of("games", list));
    }
    public Object createGame(Request req, Response res) {//probably need a dummy class here to pass on for game construction
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        GameData resultGame = service.createGame(game);
        res.status(200);
        return new Gson().toJson(Map.of("gameName:", resultGame.gameID()));
    }
    public Object joinGame(Request req, Response res) {
        //Here you're gonna want to make a custom object to get the data, then pass it on from the object
        JoinRequest request = new Gson().fromJson(req.body(), JoinRequest.class);
        service.joinGame(request.playerColor(), request.gameID());
        res.status(200);
        //String authToken = new Gson().fromJson(req.body(), String.class);
        return "";
    }
}

