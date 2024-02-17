package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import spark.*;

public class ClearHandler {

    private ClearService service;

    public ClearHandler() {
    }

    public Object clearServer(Request req, Response res) {
        service.clear();
        res.status();
        return "";
    }

    /*
    private Object deleteAllPets(Request req, Response res) throws ResponseException {
        service.deleteAllPets();
        res.status(204);
        return "";
    }

     */
}
