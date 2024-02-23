package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
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
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            if (user.username() == null || user.password() == null || user.email() == null) {
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            res.status(200);
            res.type("application/json");
            AuthData data = service.register(user);
            return new Gson().toJson(data);
        }
        catch (DataAccessException accessException) {
            res.status(403);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
    public Object login(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            AuthData returnAuth = service.login(loginRequest);
            res.status(200);
            return new Gson().toJson(returnAuth);
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }

    }
    public Object logout(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        String authToken = req.headers("authorization");
        try {
            service.logout(authToken);
            res.status(200); //implement the fail cases.
            return new Gson().toJson(null);
        }
        catch (DataAccessException accessException) {
            res.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
}

