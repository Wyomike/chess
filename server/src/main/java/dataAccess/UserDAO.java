package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData addUser(String username, String password, String email);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<UserData> listUsers();

    UserData getUser(String username);
    boolean getLogin(String username, String password);

    void deleteUserData(String username);

    void clear();
}
