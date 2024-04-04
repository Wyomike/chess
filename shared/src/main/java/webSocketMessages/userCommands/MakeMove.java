package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMove extends UserGameCommand {

    public MakeMove(String username, String authToken, int gameID, ChessMove move) {
        super(username, authToken);
        this.authToken = authToken;
        commandType = UserGameCommand.CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    //protected UserGameCommand.CommandType commandType;

    private final String authToken;
    private final int gameID;
    private final ChessMove move;

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

    public ChessMove getMove() {
        return move;
    }
}
