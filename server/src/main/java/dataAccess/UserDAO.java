package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface UserDAO {
    UserData addUser(String username, String password, String email) throws DataAccessException;
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<UserData> listUsers() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
    boolean getLogin(String username, String password) throws DataAccessException;

    void deleteUserData(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
