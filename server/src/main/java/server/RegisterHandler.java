package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import service.RegisterService;
import spark.*;

public class RegisterHandler {

    private RegisterService service;

    public RegisterHandler() {
    }

    public Object register(Request req, Response res) {//here do json stuff.
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        service.register(user);
        res.status();
        return "";
    }
}

