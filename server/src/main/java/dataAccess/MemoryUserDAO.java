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

    //protected Collection<UserData> listUsers() {
    //    return userData.values();
    //}

    @Override
    public UserData getUser(String username) {
        return userData.get(username);
    }
    @Override
//    public boolean getLogin(String username, String password) {
//        return userData.get(username).password().equals(password);
//    }
//    @Override
//    public void deleteUserData(String username) {
//        userData.remove(username);
//    }

    public void clear() {
        userData.clear();
    }
}
