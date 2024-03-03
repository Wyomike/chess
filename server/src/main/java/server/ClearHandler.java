package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import spark.*;

import java.util.Map;

public class ClearHandler {

    private final ClearService service;

    public ClearHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        service = new ClearService(authDAO, gameDAO, userDAO);
    }

    public Object clearServer(Request req, Response res) {
        try {
            service.clear();
            res.status(200);
            return new Gson().toJson(null);
        }
        catch (DataAccessException accessException) {
            res.status(500);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }
}
