package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;

import java.util.*;
import java.util.Objects;

public class GameService {
    //make call all DAOs and delete their data/delete them?
    //private final AuthDAO authDao;
    private final GameDAO gameDao;
    //private final UserDAO userDao;

    public GameService(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
        //this.authDao = authDao;
        this.gameDao = gameDao;
        //this.userDao = userDao;
    }

    public Collection<GameData> listGames() { //TODO figure out how return list.
        return gameDao.listGame();
    }
    public GameData createGame(GameData game) {
        return gameDao.addGame(game.gameName());
    }
    public void joinGame(String color, int id) {
        if(color.equals("White")) {
            gameDao.joinGame(id, color, "null");
        }
        else {
            gameDao.joinGame(id, "null", color);
        }
    }
}
