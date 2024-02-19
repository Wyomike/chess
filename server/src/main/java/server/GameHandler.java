package server;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import service.GameService;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class GameHandler {

    private GameService service;

    public GameHandler() {
    }

    public Object listGames(Request req, Response res) {//here do json stuff.
        //UserData user = new Gson().fromJson(req.body(), UserData.class);
        service.listGames();
        res.status();
        return "";
    }
    public Object createGame(Request req, Response res) {//here do json stuff.
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        service.createGame(game);
        res.status();
        return "";
    }
    public Object joinGame(Request req, Response res) {
        String request = new Gson().fromJson(req.body(), String.class);
        String color = "Black";
        String id = "1234";
        //do parse stuff?
        service.joinGame(color, id);
        //String authToken = new Gson().fromJson(req.body(), String.class);
        return "";
    }
}

