package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface AuthDAO {
    AuthData addAuth(String username) throws DataAccessException;

    //Collection<AuthData> listAuth() throws DataAccessException;

    AuthData getAuth(String id) throws DataAccessException;

    void deleteAuthData(String id) throws DataAccessException;

    void clear() throws DataAccessException;
}
