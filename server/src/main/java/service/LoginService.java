package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;

public class LoginService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public LoginService(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public void login(UserData userData) {//TODO change return type, discover what should return.
        userDao.getUser(userData.username());
        authDao.addAuth(userData.username()); //should return authToken.
    }
}
