package service;

import dataAccess.*;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(loginRequest.password());
        UserData target = userDao.getUser(loginRequest.username());
        if (target == null) throw new DataAccessException("Error: unauthorized");
        if (!encoder.matches(loginRequest.password(), target.password())) throw new DataAccessException("Error: unauthorized");
        return authDao.addAuth(loginRequest.username());
    }
    public void logout(String authToken) throws DataAccessException {
        if (authDao.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDao.deleteAuthData(authToken);
    }
}
