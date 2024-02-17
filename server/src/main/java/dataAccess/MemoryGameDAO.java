package dataAccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO {

    final private HashMap<Integer, GameData> gameData = new HashMap<>();

    public void clear() {
        gameData.clear();
    }
}
