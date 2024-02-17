package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO {

    final private HashMap<Integer, UserData> userData = new HashMap<>();

    public void clear() {
        userData.clear();
    }
}
