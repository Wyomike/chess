package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinPlayer extends UserGameCommand {
    public JoinPlayer (String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.authToken = authToken;
        commandType = UserGameCommand.CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    protected UserGameCommand.CommandType commandType;

    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public String getAuthString() {
        return authToken;
    }

    public UserGameCommand.CommandType getCommandType() {
        return this.commandType;
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

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
