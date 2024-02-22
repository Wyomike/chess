package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

public class UserService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        authDao = authDAO;
        userDao = userDAO;
    }

    public AuthData register(UserData userData) {
        userDao.addUser(userData.username(), userData.password(), userData.email());
        return authDao.addAuth(userData.username());
    }
    public AuthData login(AuthData authData) throws DataAccessException {
        if (userDao.getUser(authData.username()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return authDao.addAuth(authData.username()); //should return authToken.
    }
    public void logout(String authToken) throws DataAccessException {//TODO change return type, discover what should return.
        if (authDao.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDao.deleteAuthData(authToken);
    }
}
