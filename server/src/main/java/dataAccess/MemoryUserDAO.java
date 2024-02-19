package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> userData = new HashMap<>();
    //AuthDAO authDAO = new MemoryAuthDAO();

    public UserData addUser(String username, String password, String email) {
        UserData data = new UserData(username, password, email);
        //token = authDAO.addAuth(username);
        userData.put(username, data);
        return data;
    }

    public Collection<UserData> listUsers() {
        return userData.values();
    }

    @Override
    public UserData getUser(String username) {
        return userData.get(username);
    }
    @Override
    public boolean getLogin(String username, String password) {
        return userData.get(username).password().equals(password);
    }
    @Override
    public void deleteUserData(String username) {
        userData.remove(username);
    }

    public void clear() {
        userData.clear();
    }
}
