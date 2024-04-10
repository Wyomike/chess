package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> userData = new HashMap<>();

    public UserData addUser(String username, String password, String email) {
        UserData data = new UserData(username, password, email);
        userData.put(username, data);
        return data;
    }

    @Override
    public UserData getUser(String username) {
        return userData.get(username);
    }

    public void clear() {
        userData.clear();
    }
}
