package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData addGame(GameData gameData);
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    Collection<GameData> listGame();

    GameData getGame(int id);

    void deleteGameData(Integer id);

    void clear();
}
