package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class Error extends ServerMessage {
    //ServerMessageType serverMessageType;
    private String errorMessage;

    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Error))
            return false;
        Error that = (Error) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
