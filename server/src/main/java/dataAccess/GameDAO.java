package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData addGame(String gameName);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<GameData> listGame();

    GameData getGame(Integer id);

    void deleteGameData(String id);

    void clear();

    void joinGame(String id, String white, String black);
}
