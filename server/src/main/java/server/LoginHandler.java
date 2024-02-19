package server;

import com.google.gson.Gson;
import model.UserData;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    private LoginService service;

    public LoginHandler() {
    }

    public Object login(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        service.login(user);
        res.status();
        return "";
    }
}

