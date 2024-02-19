package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class LogoutService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public LogoutService(AuthDAO authDao, UserDAO userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    public void logout(AuthData authData) {//TODO change return type, discover what should return.
        //userDao.deleteUserData(authData.username());
        authDao.deleteAuthData(authData.authToken()); //should return authToken.
    }
}
