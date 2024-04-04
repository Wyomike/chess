package webSocketMessages.userCommands;

import java.util.Objects;

public class Resign extends UserGameCommand {

    public Resign(String username, String authToken, int gameID) {
        super(username, authToken);
        this.authToken = authToken;
        commandType = UserGameCommand.CommandType.JOIN_PLAYER;
        this.gameID = gameID;
    }

    //protected UserGameCommand.CommandType commandType;

    private final String authToken;
    private final int gameID;

    public String getAuthString() {
        return authToken;
    }

    public UserGameCommand.CommandType getCommandType() {
        return this.commandType;
    }
    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }

}
