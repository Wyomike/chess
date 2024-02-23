package service;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.*;
import java.util.Objects;

public class GameService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    //private final UserDAO userDao;

    public GameService(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
        //this.userDao = userDao;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException { //TODO figure out how return list.
        if (authDao.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        return gameDao.listGame();
    }
    public GameData createGame(GameData game, String authToken) throws DataAccessException {
        if (authDao.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        return gameDao.addGame(game.gameName());
    }
    public void joinGame(String color, int id, String authToken) throws DataAccessException {
        if (authDao.getAuth(authToken) == null) throw new DataAccessException("Error: unauthorized");
        if (gameDao.getGame(id) == null) throw new BadRequestException("Error: bad request");
        if (color != null && color.equals("WHITE")) {
            gameDao.joinGame(id, authDao.getAuth(authToken).username(), null);
        }
        else if (color != null && color.equals("BLACK")) {
            gameDao.joinGame(id, null, authDao.getAuth(authToken).username());
        }
        else if (color == null) {
            gameDao.joinGame(id, null, null);
        }
    }
}
