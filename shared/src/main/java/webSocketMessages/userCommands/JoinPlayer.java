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
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
