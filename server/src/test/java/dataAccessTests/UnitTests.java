package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import service.GameService;
import service.UserService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTests {

    private UserData testUser = new UserData("username", "password", "email");
    private UserData newTestUser = new UserData("1", "2", "3");

    private static AuthDAO authDAO = new MemoryAuthDAO();
    private static UserDAO userDAO = new MemoryUserDAO();
    private static GameDAO gameDAO = new MemoryGameDAO();

    private static ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
    private static UserService userService = new UserService(authDAO, userDAO);
    private static GameService gameService = new GameService(authDAO, gameDAO);

    private static Server server;
    private String existingAuth;

    @BeforeEach //WILL NEED TO INITIALIZE DATABASE
    public void init() {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new MemoryGameDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clearTest() throws Exception {
        userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
        userDAO.addUser(newTestUser.username(), newTestUser.password(), newTestUser.email());
        String id1 = authDAO.addAuth(testUser.username()).authToken();
        String id2 = authDAO.addAuth(newTestUser.username()).authToken();
        int gameID = gameDAO.addGame("game").gameID();
        gameDAO.joinGame(gameID, testUser.username(), null);
        clearService.clear();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(userDAO.getUser(newTestUser.username()));
        Assertions.assertNull(authDAO.getAuth(id1));
        Assertions.assertNull(authDAO.getAuth(id2));
        Assertions.assertNull(gameDAO.getGame(0));
    }

    @Test
    @Order(2)
    @DisplayName("Register")
    public void registerTest() throws Exception {
        AuthData result = userService.register(testUser);
        Assertions.assertEquals(result.username(), testUser.username());
        Assertions.assertNotNull(result.authToken());
    }
    @Test
    @Order(3)
    @DisplayName("Bad Register")
    public void badRegisterTest() throws Exception {
        try {
            AuthData result = userService.register(testUser);
            AuthData doubleResult = userService.register(testUser);
            Assertions.assertEquals(result.username(), testUser.username());
            Assertions.assertNotNull(result.authToken());
        }
        catch(DataAccessException accessException) {
            Assertions.assertEquals("Error: already taken", accessException.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login")
    public void loginTest() throws Exception {
        userService.register(testUser);
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        AuthData result = userService.login(loginRequest);
        Assertions.assertEquals(result.username(), testUser.username());
        Assertions.assertNotNull(result.authToken());
    }
    @Test
    @Order(5)
    @DisplayName("Bad Login")
    public void badLoginTest() throws Exception {
        try {
            userService.register(testUser);
            LoginRequest loginRequest = new LoginRequest("who?", "What?");
            userService.login(loginRequest);
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Logout")
    public void LogoutTest() throws Exception {
        userService.register(testUser);
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        String authToken = userService.login(loginRequest).authToken();
        userService.logout(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }
    @Test
    @Order(7)
    @DisplayName("Logout Nonexistant")
    public void badLogoutTest() throws Exception {
        try {
            userService.register(testUser);
            LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
            String authToken = userService.login(loginRequest).authToken();
            userService.logout("a");
            Assertions.assertNull(authDAO.getAuth(authToken));
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Create game")
    public void createGame() throws Exception {
        AuthData auth1 = userService.register(testUser);
        AuthData auth2 = userService.register(newTestUser);
        int id = gameService.createGame(gameDAO.addGame("game"), auth1.authToken()).gameID();
        gameDAO.joinGame(id, auth1.username(), auth2.username());
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        Assertions.assertEquals(2, id);
    }
    @Test
    @Order(9)
    @DisplayName("Create game with invalid auth")
    public void badCreateGame() throws Exception {
        try {
            AuthData auth1 = userService.register(testUser);
            AuthData auth2 = userService.register(newTestUser);
            int id = gameService.createGame(gameDAO.addGame("game"), "???").gameID();
            gameDAO.joinGame(1, auth1.username(), auth2.username());
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
            Assertions.assertEquals(2, id);
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }

    @Test
    @Order(10)
    @DisplayName("Join game")
    public void joinGame() throws Exception {
        AuthData auth1 = userService.register(testUser);
        AuthData auth2 = userService.register(newTestUser);
        int id = gameService.createGame(gameDAO.addGame("game"), auth1.authToken()).gameID();
        gameService.joinGame("WHITE", id, auth1.authToken());
        gameService.joinGame("BLACK", id, auth2.authToken());
        Assertions.assertEquals(2, id);
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
    }
    @Test
    @Order(11)
    @DisplayName("Join bad game")
    public void badJoinGame() throws Exception {
        try {
            AuthData auth1 = userService.register(testUser);
            AuthData auth2 = userService.register(newTestUser);
            int id = gameService.createGame(gameDAO.addGame("game"), auth1.authToken()).gameID();
            gameService.joinGame("WHITE", 4, auth1.authToken());
            gameService.joinGame("BLACK", id, auth2.authToken());
            Assertions.assertEquals(2, id);
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: bad request", accessException.getMessage());
        }
    }

    @Test
    @Order(12)
    @DisplayName("List games")
    public void listGame() throws Exception {
        AuthData auth1 = userService.register(testUser);
        AuthData auth2 = userService.register(newTestUser);
        int id = gameService.createGame(gameDAO.addGame("game"), auth1.authToken()).gameID();
        gameService.joinGame("WHITE", id, auth1.authToken());
        gameService.joinGame("BLACK", id, auth2.authToken());
        Assertions.assertEquals(2, id);
        Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
        Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
        gameService.listGames(auth1.authToken());
    }
    @Test
    @Order(13)
    @DisplayName("List games with bad auth")
    public void badListGame() throws Exception {
        try {
            AuthData auth1 = userService.register(testUser);
            AuthData auth2 = userService.register(newTestUser);
            int id = gameService.createGame(gameDAO.addGame("game"), auth1.authToken()).gameID();
            gameService.joinGame("WHITE", id, auth1.authToken());
            gameService.joinGame("BLACK", id, auth2.authToken());
            Assertions.assertEquals(2, id);
            Assertions.assertEquals(testUser.username(), gameDAO.getGame(id).whiteUsername());
            Assertions.assertEquals(newTestUser.username(), gameDAO.getGame(id).blackUsername());
            gameService.listGames("whop whop");
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }

}
