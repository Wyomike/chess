package clientTests;

import dataAccess.*;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    int port = -1;

    private static AuthDAO authDAO = new SQLAuthDAO();
    private static UserDAO userDAO = new SQLUserDAO();
    private static GameDAO gameDAO = new SQLGameDAO();

    private static ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
    private static UserService userService = new UserService(authDAO, userDAO);
    private static GameService gameService = new GameService(authDAO, gameDAO);


    private UserData testUser = new UserData("username", "password", "email");
    private UserData newTestUser = new UserData("1", "2", "3");


    @BeforeEach
    public void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);

        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);

        try {
            clearService.clear();
        }
        catch (DataAccessException accessException) {
            System.out.println(accessException.getMessage());
        }
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clearTest() throws Exception {
        AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
        AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
        facade.createGame("game", auth1.authToken());
        facade.joinGame("WHITE", 1, auth1.authToken());
        facade.joinGame("BLACK", 1, auth2.authToken());
        clearService.clear();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(userDAO.getUser(newTestUser.username()));
        Assertions.assertNull(authDAO.getAuth(auth1.authToken()));
        Assertions.assertNull(authDAO.getAuth(auth2.authToken()));
        Assertions.assertNull(gameDAO.getGame(1));
    }

    @Test
    @Order(2)
    @DisplayName("Register")
    public void registerTest() throws Exception {
        AuthData result = facade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertEquals(result.username(), testUser.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(testUser.email(), userDAO.getUser(testUser.username()).email());
    }
    @Test
    @Order(3)
    @DisplayName("Bad Register")
    public void badRegisterTest() throws Exception {
        try {
            AuthData result = facade.register(testUser.username(), testUser.password(), testUser.email());
            AuthData doubleResult = facade.register(testUser.username(), testUser.password(), testUser.email());
            Assertions.assertEquals(result.username(), testUser.username());
            Assertions.assertNotNull(result.authToken());
        }
        catch(IOException responseException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 403 for URL: http://localhost:" + stringport + "/user";
            Assertions.assertEquals(expected, responseException.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login")
    public void loginTest() throws Exception {
        facade.register(testUser.username(), testUser.password(), testUser.email());
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        AuthData result = facade.login(loginRequest.username(), loginRequest.password());
        Assertions.assertEquals(result.username(), testUser.username());
        Assertions.assertNotNull(result.authToken());
    }
    @Test
    @Order(5)
    @DisplayName("Bad Login")
    public void badLoginTest() throws Exception {
        try {
            facade.register(testUser.username(), testUser.password(), testUser.email());
            LoginRequest loginRequest = new LoginRequest("who?", "What?");
            facade.login(loginRequest.username(), loginRequest.password());
        }
        catch (IOException responseException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 401 for URL: http://localhost:" + stringport + "/session";
            Assertions.assertEquals(expected, responseException.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Logout")
    public void LogoutTest() throws Exception {
        facade.register(testUser.username(), testUser.password(), testUser.email());
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        String authToken = facade.login(loginRequest.username(), loginRequest.password()).authToken();
        facade.logout(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }
    @Test
    @Order(7)
    @DisplayName("Logout Nonexistant")
    public void badLogoutTest() throws Exception {
        try {
            facade.register(testUser.username(), testUser.password(), testUser.email());
            LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
            String authToken = facade.login(loginRequest.username(), loginRequest.password()).authToken();
            facade.logout("a");
            Assertions.assertNull(authDAO.getAuth(authToken));
        }
        catch (IOException responseException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 401 for URL: http://localhost:" + stringport + "/session";
            Assertions.assertEquals(expected, responseException.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Create game")
    public void createGame() throws Exception {
        AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
        AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
        int id = facade.createGame("game", auth1.authToken()).gameID();
        gameDAO.joinGame(id, auth1.username(), auth2.username());
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        Assertions.assertEquals(1, id);
    }
    @Test
    @Order(9)
    @DisplayName("Create game with invalid auth")
    public void badCreateGame() throws Exception {
        try {
            AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
            AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
            int id = facade.createGame("game", "???").gameID();
            gameDAO.joinGame(1, auth1.username(), auth2.username());
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
            Assertions.assertEquals(2, id);
        }
        catch (IOException responseException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 401 for URL: http://localhost:" + stringport + "/game";
            Assertions.assertEquals(expected, responseException.getMessage());
        }
    }

    @Test
    @Order(10)
    @DisplayName("Join game")
    public void joinGame() throws Exception {
        AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
        AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
        int id = facade.createGame("game", auth1.authToken()).gameID();
        facade.joinGame("WHITE", id, auth1.authToken());
        facade.joinGame("BLACK", id, auth2.authToken());
        Assertions.assertEquals(1, id);
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
    }
    @Test
    @Order(11)
    @DisplayName("Join bad game")
    public void badJoinGame() throws Exception {
        try {
            AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
            AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
            int id = facade.createGame("game", auth1.authToken()).gameID();
            facade.joinGame("WHITE", 4, auth1.authToken());
            facade.joinGame("BLACK", id, auth2.authToken());
            Assertions.assertEquals(2, id);
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        }
        catch (IOException accessException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 400 for URL: http://localhost:" + stringport + "/game";
            Assertions.assertEquals(expected, accessException.getMessage());
        }
    }

    @Test
    @Order(12)
    @DisplayName("List games")
    public void listGame() throws Exception {
        AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
        AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
        int id = facade.createGame("game", auth1.authToken()).gameID();
        facade.joinGame("WHITE", id, auth1.authToken());
        facade.joinGame("BLACK", id, auth2.authToken());
        Assertions.assertEquals(1, id);
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        facade.listGames(auth1.authToken());
    }
    @Test
    @Order(13)
    @DisplayName("List games with bad auth")
    public void badListGame() throws Exception {
        try {
            AuthData auth1 = facade.register(testUser.username(), testUser.password(), testUser.email());
            AuthData auth2 = facade.register(newTestUser.username(), newTestUser.password(), newTestUser.email());
            int id = facade.createGame("game", auth1.authToken()).gameID();
            facade.joinGame("WHITE", id, auth1.authToken());
            facade.joinGame("BLACK", id, auth2.authToken());
            Assertions.assertEquals(1, id);
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
            facade.listGames("whop whop");
        }
        catch (IOException accessException) {
            String stringport = String.valueOf(port);
            String expected = "Server returned HTTP response code: 401 for URL: http://localhost:" + stringport + "/game";
            Assertions.assertEquals(expected, accessException.getMessage());
        }
    }

    @Test
    @Order(14)
    @DisplayName("Clear empty")
    public void emptyClearTest() throws Exception {
        clearService.clear();
        //Doing this for testing reasons
        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(userDAO.getUser(newTestUser.username()));
        Assertions.assertNull(gameDAO.getGame(1));
    }

}
