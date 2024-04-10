package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    public JoinObserver(String username, String authToken, int gameID) {
        super(username, authToken);
        commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    private final int gameID;

    public int getGameID() {
        return gameID;
    }
}
