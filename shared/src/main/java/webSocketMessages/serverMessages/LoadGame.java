package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class LoadGame extends ServerMessage {
    //ServerMessageType serverMessageType;
    private final int game;

    public LoadGame(int gameID) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gameID;
    }

    public int getGame() {
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

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
