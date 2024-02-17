package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    //private AuthData authData;
    final private HashMap<Integer, AuthData> authData = new HashMap<>();

    public void clear() {
        authData.clear();
    }
}
