package service;

import dataAccess.*;
import model.GameData;
import java.util.*;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException { //TODO figure out how return list.
        if (authDAO.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        return gameDAO.listGame();
    }
    public GameData createGame(GameData game, String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        return gameDAO.addGame(game.gameName());
    }
    public void joinGame(String color, int id, String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        if (gameDAO.getGame(id) == null) throw new BadRequestException("Error: bad request");
        if (color != null && color.equals("WHITE")) {
            gameDAO.joinGame(id, authDAO.getAuth(authToken).username(), null);
        }
        else if (color != null && color.equals("BLACK")) {
            gameDAO.joinGame(id, null, authDAO.getAuth(authToken).username());
        }
        else if (color == null) {
            gameDAO.joinGame(id, null, null);
        }
    }
}
