package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;

public class RegisterService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public RegisterService(AuthDAO authDao, GameDAO gameDao, UserDAO userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public void register(UserData userData) {//TODO change return type, discover what should return. bool?
        userDao.addUser(userData.username(), userData.password(), userData.email());
        authDao.addAuth(userData.username());
    }
}
