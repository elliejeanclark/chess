package websocket.messages;

import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {
    private String game;

    public LoadGameMessage(ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }

    public void setGame(String message) {
        this.game = message;
    }

    public String getGame() {
        return game;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
