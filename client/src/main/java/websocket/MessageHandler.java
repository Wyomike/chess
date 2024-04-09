package websocket;

import webSocketMessages.userCommands.UserGameCommand;

public interface CommandHandler {
    void notify(UserGameCommand notification);
}
