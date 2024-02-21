package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

public class UserService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao = new MemoryAuthDAO();
    private final UserDAO userDao = new MemoryUserDAO();

    public UserService() {
    }

    public AuthData register(UserData userData) {
        userDao.addUser(userData.username(), userData.password(), userData.email());
        return authDao.addAuth(userData.username());
    }
    public AuthData login(AuthData authData) {
        userDao.getUser(authData.username());
        return authDao.addAuth(authData.username()); //should return authToken.
    }
    public void logout(String authToken) {//TODO change return type, discover what should return.
        authDao.deleteAuthData(authToken); //should return authToken.
    }
}
