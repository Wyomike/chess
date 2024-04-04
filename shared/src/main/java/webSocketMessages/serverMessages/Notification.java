package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class Notification extends ServerMessage {
    //ServerMessageType serverMessageType;
    private final String message;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        //this.serverMessageType = ServerMessageType.NOTIFICATION;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Notification))
            return false;
        Notification that = (Notification) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
