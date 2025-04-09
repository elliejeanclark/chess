package server.serverwebsocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Integer> usersInGame = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
        usersInGame.put(username, gameID);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
        usersInGame.remove(visitorName);
    }

    public void broadcast(String excludeUsername, ServerMessage message, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername) && gameID == usersInGame.get(c.username)) {
                    c.send(message.toString());
                }
            }
            else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void broadcastWhitePerspective(String excludeBlack, ServerMessage message, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeBlack)  && gameID == usersInGame.get(c.username)) {
                    c.send(message.toString());
                }
            }
            else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void broadcastToBlackPlayer(String username, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(message.toString());
                }
            }
            else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
