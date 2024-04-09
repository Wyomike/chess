package webSocketMessages.serverMessages;

import model.GameData;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class LoadGame extends ServerMessage {
    //ServerMessageType serverMessageType;
    private final GameData game;
    private final int gameID;

    public LoadGame(GameData game, int gameID) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
    public GameData getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LoadGame))
            return false;
        LoadGame that = (LoadGame) o;
        return getServerMessageType() == that.getServerMessageType();
    }

}
