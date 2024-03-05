package dataAccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    GameData addGame(String gameName) throws DataAccessException;
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<GameData> listGame() throws DataAccessException;

    GameData getGame(Integer id) throws DataAccessException;

    void deleteGameData(int id) throws DataAccessException;

    void clear() throws DataAccessException;

    void joinGame(int id, String white, String black) throws DataAccessException;
}
