package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinPlayer extends UserGameCommand {
    public JoinPlayer (String username, String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(username, authToken);
        commandType = UserGameCommand.CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public JoinPlayer (JoinPlayer old) {
        super(old.getUsername(), old.getAuthString());
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = old.getGameID();
        this.playerColor = old.getPlayerColor();
    }

    //protected UserGameCommand.CommandType commandType;

//    private final String username;
//    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

//    public String getUsername() {
//        return username;
//    }
//    public String getAuthString() {
//        return authToken;
//    }

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
