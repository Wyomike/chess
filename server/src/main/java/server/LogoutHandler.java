package server;

import com.google.gson.Gson;
import model.AuthData;
import service.LoginService;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    private LogoutService service;

    public LogoutHandler() {
    }

    public Object logout(Request req, Response res) {//here do json stuff. this may cause issue with missing info.
        AuthData auth = new Gson().fromJson(req.body(), AuthData.class);
        service.logout(auth);
        res.status();
        return "";
    }
}

