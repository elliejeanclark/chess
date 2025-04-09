package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage{
    private String message;

    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
