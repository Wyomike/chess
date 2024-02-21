package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class UserHandler {

    private UserService service = new UserService();

    public UserHandler() {
    }

    public Object register(Request req, Response res) {//here do json stuff.
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        res.status(200);
        res.type("application/json");
        AuthData data = service.register(user);
        return new Gson().toJson(Map.of("AuthData", data));
    }
    public Object login(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        AuthData authData = new Gson().fromJson(req.body(), AuthData.class);
        service.login(authData);
        res.status(200);
        AuthData returnAuth = service.login(authData);
        return new Gson().toJson(Map.of("AuthData", returnAuth));
    }
    public Object logout(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        String authToken = new Gson().fromJson(req.body(), String.class);
        service.logout(authToken);
        res.status(200); //implement the fail cases.
        return "";
    }
}

