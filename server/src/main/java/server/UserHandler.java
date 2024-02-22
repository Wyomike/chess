package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class UserHandler {

    private UserService service;

    public UserHandler(AuthDAO authDAO, UserDAO userDAO) {
        service = new UserService(authDAO, userDAO);
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
        try {
            AuthData returnAuth = service.login(authData);
            res.status(200);
            System.out.println("1");
            return new Gson().toJson(Map.of("AuthData", returnAuth));
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("Message", accessException.getMessage()));
        }

    }
    public Object logout(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        String authToken = req.headers("authorization");
        try {
            service.logout(authToken);
            res.status(200); //implement the fail cases.
            return new Gson().toJson("");
        }
        catch (DataAccessException accessException) {
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
}

