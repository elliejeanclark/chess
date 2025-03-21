package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import reqandres.*;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() {
        facade = new ServerFacade("http://localhost:8080");
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
}
