package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;

public class UserService {
    //make call all DAOs and delete their data/delete them?
    private final AuthDAO authDao;
    private final UserDAO userDao;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        authDao = authDAO;
        userDao = userDAO;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        if (userDao.getUser(userData.username()) != null) throw new DataAccessException("Error: already taken");
        userDao.addUser(userData.username(), userData.password(), userData.email());
        return authDao.addAuth(userData.username());
    }
    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        if (userDao.getUser(loginRequest.username()) == null) throw new DataAccessException("Error: unauthorized");
        if (!(userDao.getUser(loginRequest.username()).password().equals(loginRequest.password()))) throw new DataAccessException("Error: unauthorized");
        return authDao.addAuth(loginRequest.username()); //should return authToken.
    }
    public void logout(String authToken) throws DataAccessException {//TODO change return type, discover what should return.
        if (authDao.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDao.deleteAuthData(authToken);
    }
}
