package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;

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

    public void listGames() { //TODO figure out how return list.
        gameDao.listGame();
    }
    public void createGame(GameData game) {
        gameDao.addGame(game.gameName());
    }
    public void joinGame(String color, String id) {
        if(color.equals("White")) {
            gameDao.joinGame(id, color, "null");
        }
    }
}
