package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, AuthData> authsData = new HashMap<>();

    public AuthData addAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData data = new AuthData(token, username);
        authsData.put(token, data);
        return data;
    }

    @Override
    public AuthData getAuth(String id) {
        return authsData.get(id);
    }
    @Override
    public void deleteAuthData(String id) {
        authsData.remove(id);
    }

    public void clear() {
        authsData.clear();
    }
}
