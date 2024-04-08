package webSocketMessages.userCommands;

import java.util.Objects;

public class Resign extends UserGameCommand {

    public Resign(String username, String authToken, int gameID) {
        super(username, authToken);
        commandType = UserGameCommand.CommandType.JOIN_PLAYER;
        this.gameID = gameID;
    }

    private final int gameID;

    public int getGameID() {
        return gameID;
    }

}
