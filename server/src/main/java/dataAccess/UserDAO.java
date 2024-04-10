package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface UserDAO {
    UserData addUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
