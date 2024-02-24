package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import spark.*;

public class ClearHandler {

    private final ClearService service;

    public ClearHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        service = new ClearService(authDAO, gameDAO, userDAO);
    }

    public Object clearServer(Request req, Response res) {
        service.clear();
        res.status(200);
        return new Gson().toJson(null);
    }
}
