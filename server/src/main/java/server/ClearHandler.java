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
        res.status(200);
        return "";
    }
}
