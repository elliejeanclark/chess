package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public void setMessage(String message) {
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
