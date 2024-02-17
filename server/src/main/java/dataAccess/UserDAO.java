package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData addUser(UserData userData);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<UserData> listUsers();

    UserData getUser(int id);

    void deleteUserData(Integer id);

    void clear();
}