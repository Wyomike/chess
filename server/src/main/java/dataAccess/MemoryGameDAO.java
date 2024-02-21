package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO { //How will we identify games? will need to determine this.

    int numGames = 0;
    final private HashMap<Integer, GameData> gameData = new HashMap<>();

    public GameData addGame(String gameName) {
        int gameID = ++numGames;
        GameData data = new GameData(gameID, "None", "None", gameName, new ChessGame());
        gameData.put(gameID, data);
        return data;
    }
    private void updateGame(GameData game, String white, String  black) {
        GameData data = new GameData(game.gameID(), white, black, game.gameName(), game.game());
        gameData.put(game.gameID(), data);
        //return data;
    }

    public Collection<GameData> listGame() {
        return gameData.values();
    }
    @Override
    public GameData getGame(Integer id) {
        return gameData.get(id);
    }
    public void joinGame(int id, String white, String black) {
        updateGame(getGame(id), white, black);
        deleteGameData(id);
    }
    @Override
    public void deleteGameData(int id) {
        gameData.remove(id);
    }

    public void clear() {
        gameData.clear();
    }
}
