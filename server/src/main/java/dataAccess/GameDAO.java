package dataAccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    GameData addGame(String gameName);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<GameData> listGame();

    GameData getGame(Integer id);

    void deleteGameData(int id);

    void clear();

    void joinGame(int id, String white, String black) throws DataAccessException;
}
