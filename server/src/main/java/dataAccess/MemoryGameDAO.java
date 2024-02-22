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
        GameData data = new GameData(gameID, "null", "null", gameName, new ChessGame());
        gameData.put(gameID, data);
        return data;
    }
    private GameData updateGame(GameData game, String white, String  black) {
        GameData data = new GameData(game.gameID(), white, black, game.gameName(), game.game());
        if (white.equals("null")) {
            return data = new GameData(game.gameID(), game.whiteUsername(), black, game.gameName(), game.game());
        }
        else if (black.equals("null")) {
            return data = new GameData(game.gameID(), white, game.blackUsername(), game.gameName(), game.game());
        }
        return data;
    }

    public Collection<GameData> listGame() {
        return gameData.values();
    }
    @Override
    public GameData getGame(Integer id) {
        return gameData.get(id);
    }
    public void joinGame(int id, String white, String black) {
        GameData tempGame = updateGame(getGame(id), white, black);
        deleteGameData(id);
        gameData.put(tempGame.gameID(), tempGame);
    }
    @Override
    public void deleteGameData(int id) {
        gameData.remove(id);
    }

    public void clear() {
        gameData.clear();
    }
}
