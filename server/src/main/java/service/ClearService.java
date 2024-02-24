package service;

import dataAccess.*;

public class ClearService {
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private final UserDAO userDao;

    public ClearService(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
        this.userDao = userDao;
    }

    public void clear() {
        authDao.clear();
        gameDao.clear();
        userDao.clear();
    }
}
