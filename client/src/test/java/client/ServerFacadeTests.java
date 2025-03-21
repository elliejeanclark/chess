package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import reqandres.*;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() {
        String serverURL = "http://localhost:";
        serverURL += port;
        facade = new ServerFacade(serverURL);
        try {
            facade.clear();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testGoodRegister() throws ResponseException {
        RegisterResult result = facade.register("bob", "bob", "bob");
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testBadRegister() throws ResponseException {
        facade.register("bob", "bob", "bob@gmail.com");
        Assertions.assertThrows(ResponseException.class, () -> facade.register("bob", "bob", "bob@gmail.com"));
    }

    @Test
    public void testGoodLogin() throws ResponseException {
        facade.register("bob", "bob", "bob@gmail.com");
        LoginResult result = facade.login("bob", "bob");
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testBadLogin(){
        Assertions.assertThrows(ResponseException.class, () -> facade.login("bob", "bob"));
    }

    @Test
    public void testGoodCreate() throws ResponseException {
        RegisterResult result = facade.register("bob", "bob", "bob@gmail.com");
        String authToken = result.authToken();
        CreateGameResult createResult = facade.create(authToken, "testgame");
        Assertions.assertNull(createResult.message());
    }

    @Test
    public void testBadCreate() {
        Assertions.assertThrows(ResponseException.class, () -> facade.create(null, "testgame"));
    }

    @Test
    public void testGoodLogout() throws ResponseException {
        RegisterResult registerResult = facade.register("bob", "bob", "bob@gmail.com");
        String authToken = registerResult.authToken();
        LogoutResult result = facade.logout(authToken);
        Assertions.assertNull(result.message());
    }

    @Test
    public void testBadLogout() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(null));
    }

    @Test
    public void testGoodList() throws ResponseException {
        RegisterResult registerResult = facade.register("bob", "bob", "bob@gmail.com");
        String authToken = registerResult.authToken();
        facade.create(authToken, "testgame");
        ListGamesResult result = facade.list(authToken);
        Assertions.assertNull(result.message());
    }

    @Test
    public void testBadList() {
       Assertions.assertThrows(ResponseException.class, () -> facade.list(null));
    }

    @Test
    public void testGoodJoin() throws ResponseException {
        RegisterResult registerResult = facade.register("bob", "bob", "bob@gmail.com");
        String authToken = registerResult.authToken();
        facade.create(authToken, "testgame");
        JoinGameResult result = facade.join(authToken, 1, ChessGame.TeamColor.WHITE);
        Assertions.assertNull(result.message());
    }

    @Test
    public void testBadJoin() {
        Assertions.assertThrows(ResponseException.class, () -> facade.join(null, 0, null));
    }
}
