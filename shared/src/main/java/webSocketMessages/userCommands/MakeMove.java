package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMove extends UserGameCommand {

    public MakeMove(String username, String authToken, int gameID, ChessMove move) {
        super(username, authToken);
        commandType = UserGameCommand.CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    private final int gameID;
    private final ChessMove move;

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
