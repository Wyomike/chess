package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO { //determine which functions need, for now just copied over from petshop
    AuthData addAuth(AuthData authData);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<AuthData> listAuth();

    AuthData getAuth(int id);

    void deleteAuthData(Integer id);

    void clear();
}
