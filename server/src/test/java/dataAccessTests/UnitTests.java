package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;
import server.Server;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.utils.Assert;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTests {

    private UserData testUser = new UserData("username", "password", "email");
    private UserData newTestUser = new UserData("1", "2", "3");

    private static AuthDAO authDAO = new SQLAuthDAO();
    private static UserDAO userDAO = new SQLUserDAO();
    private static GameDAO gameDAO = new SQLGameDAO();

    private static ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
    private static UserService userService = new UserService(authDAO, userDAO);
    private static GameService gameService = new GameService(authDAO, gameDAO);

    private static Server server;
    private String existingAuth;

    @BeforeEach
    public void init() {
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

    @Test
    @Order(1)
    @DisplayName("User Clear")
    public void userClearTest() throws Exception {
        userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
        userDAO.addUser(newTestUser.username(), newTestUser.password(), newTestUser.email());
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(userDAO.getUser(newTestUser.username()));
    }
    @Test
    @Order(2)
    @DisplayName("Add User")
    public void addUserTest() throws Exception {
        userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
        UserData result = userDAO.getUser(testUser.username());
        Assertions.assertEquals(result.username(), testUser.username());
    }
    @Test
    @Order(3)
    @DisplayName("Bad Add User")
    public void badAddUserTest() throws Exception {
        try {
            userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
            userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
            UserData result = userDAO.getUser(testUser.username());
        }
        catch(DataAccessException accessException) {
            Assertions.assertEquals("Error: already taken", accessException.getMessage());
        }
    }
    @Test
    @Order(4)
    @DisplayName("Get User")
    public void getUserTest() throws Exception {
        UserData user1 = userDAO.addUser(testUser.username(), testUser.password(), testUser.email());
        UserData user2 = userDAO.addUser(newTestUser.username(), newTestUser.password(), newTestUser.email());
        UserData result = userDAO.getUser(testUser.username());
        UserData result2 = userDAO.getUser(newTestUser.username());
        Assertions.assertEquals(user1, result);
        Assertions.assertEquals(user2, result2);
    }
    @Test
    @Order(5)
    @DisplayName("Get nonexistent user")
    public void badGetUserTest() throws Exception {
        try {
            UserData result = userDAO.getUser(testUser.username());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Clear Auth")
    public void clearAuthTest() throws Exception {
        authDAO.addAuth(testUser.username());
        authDAO.addAuth(newTestUser.username());
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(testUser.username()));
        Assertions.assertNull(userDAO.getUser(newTestUser.username()));
    }
    @Test
    @Order(7)
    @DisplayName("Add Auth")
    public void addAuthTest() throws Exception {
        AuthData testAuth = authDAO.addAuth(testUser.username());
        AuthData result = authDAO.getAuth(testAuth.authToken());
        Assertions.assertEquals(testAuth, result);
    }
    @Test
    @Order(8)
    @DisplayName("Add bad auth")
    public void badAddAuthTest() throws Exception {
        try {
            AuthData testAuth = authDAO.addAuth("");
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: unauthorized", accessException.getMessage());
        }
    }
    @Test
    @Order(9)
    @DisplayName("Get auth")
    public void getAuthTest() throws Exception {
        AuthData test = authDAO.addAuth("test");
        AuthData result = authDAO.getAuth(test.authToken());
        Assertions.assertEquals(test, result);
    }
    @Test
    @Order(10)
    @DisplayName("Get bad auth")
    public void getBadAuthTest() throws Exception {
        try {
            AuthData result = authDAO.getAuth("test.authToken()");
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("authToken DNE", accessException.getMessage());
        }

    }
    @Test
    @Order(11)
    @DisplayName("Delete auth")
    public void delAuthTest() throws Exception {
        try {
            AuthData result = authDAO.addAuth(testUser.username());
            authDAO.deleteAuthData(result.authToken());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("authToken DNE", accessException.getMessage());
        }
    }
    @Test
    @Order(12)
    @DisplayName("Delete bad auth")
    public void delBadAuthTest() throws Exception {
        try {
            AuthData result = authDAO.addAuth(testUser.username());
            authDAO.deleteAuthData("huh");
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("authToken DNE", accessException.getMessage());
        }
    }

    @Test
    @Order(13)
    @DisplayName("Create game")
    public void createGameTest() throws Exception {
        GameData game = gameDAO.addGame("gaaame");
        GameData game2 = gameDAO.addGame("game");
        GameData result = gameDAO.getGame(game.gameID());
        Assertions.assertEquals(game.gameName(), result.gameName());
        Assertions.assertEquals(game.gameID(), result.gameID());
        Assertions.assertEquals(game.blackUsername(), result.blackUsername());
        Assertions.assertEquals(game.whiteUsername(), result.whiteUsername());
        Assertions.assertEquals(2, gameDAO.getGame(game2.gameID()).gameID());
    }
    @Test
    @Order(13)
    @DisplayName("Create bad game")
    public void createBadGameTest() throws Exception {
        try {
            GameData game = gameDAO.addGame("");
            GameData game2 = gameDAO.addGame("game");
            GameData result = gameDAO.getGame(game.gameID());
            Assertions.assertEquals(game.gameName(), result.gameName());
            Assertions.assertEquals(game.gameID(), result.gameID());
            Assertions.assertEquals(game.blackUsername(), result.blackUsername());
            Assertions.assertEquals(game.whiteUsername(), result.whiteUsername());
            Assertions.assertEquals(2, gameDAO.getGame(game2.gameID()).gameID());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: no name", accessException.getMessage());
        }
    }
    @Test
    @Order(14)
    @DisplayName("Clear games")
    public void clearGamesTest() throws Exception {
        GameData game = gameDAO.addGame("game");
        gameDAO.clear();
        GameData result = gameDAO.getGame(game.gameID());
        Assertions.assertNull(result);
    }
    @Test
    @Order(14)
    @DisplayName("Get game")
    public void getGameTest() throws Exception {
        GameData game = gameDAO.addGame("game");
        GameData game2 = gameDAO.addGame("game2");
        GameData result = gameDAO.getGame(game.gameID());
        Assertions.assertEquals(game.gameName(), result.gameName());
        Assertions.assertEquals(game.gameID(), result.gameID());
        Assertions.assertEquals(game.blackUsername(), result.blackUsername());
        Assertions.assertEquals(game.whiteUsername(), result.whiteUsername());
    }
    @Test
    @Order(15)
    @DisplayName("Get bad game")
    public void getBadGameTest() throws Exception {
        GameData game = gameDAO.addGame("game");
        GameData game2 = gameDAO.addGame("game2");
        GameData result = gameDAO.getGame(3);
        Assertions.assertNull(result);
    }
    @Test
    @Order(16)
    @DisplayName("List games")
    public void listGameTest() throws Exception {
        GameData game = gameDAO.addGame("game");
        GameData game2 = gameDAO.addGame("game2");
        GameData game3 = gameDAO.addGame("game3");
        gameDAO.joinGame(3, "Hi", null);
        gameDAO.joinGame(2, null, "hey");
        Collection<GameData> games = new ArrayList<>();
        games.add(game);
        games.add(game2);
        games.add(game3);
        Collection<GameData> result = gameDAO.listGame();
        Assertions.assertEquals(games.size(), result.size());
    }
    @Test
    @Order(17)
    @DisplayName("List no games")
    public void listNoGameTest() throws Exception {
        GameData game = gameDAO.addGame("game");
        GameData game2 = gameDAO.addGame("game2");
        GameData game3 = gameDAO.addGame("game3");
        gameDAO.joinGame(3, "Hi", null);
        gameDAO.joinGame(2, null, "hey");
        gameDAO.clear();
        Collection<GameData> games = new ArrayList<>();
        Collection<GameData> result = gameDAO.listGame();
        Assertions.assertEquals(games.size(), result.size());
    }
    @Test
    @Order(18)
    @DisplayName("Join game")
    public void joinGameTest() throws Exception {
        gameDAO.addGame("game");
        gameDAO.addGame("game2");
        gameDAO.addGame("game3");
        gameDAO.joinGame(3, "Hi", null);
        gameDAO.joinGame(2, null, "hey");
        gameDAO.joinGame(1, "1", "2");
        GameData game1 = gameDAO.getGame(1);
        GameData game2 = gameDAO.getGame(2);
        GameData game3 = gameDAO.getGame(3);
        Assertions.assertEquals("1", game1.whiteUsername());
        Assertions.assertEquals("2", game1.blackUsername());
        Assertions.assertEquals("Hi", game3.whiteUsername());
        Assertions.assertNull(game3.blackUsername());
        Assertions.assertNull(game2.whiteUsername());
        Assertions.assertEquals("hey", game2.blackUsername());
    }
    @Test
    @Order(19)
    @DisplayName("Join bad game")
    public void joinBadGameTest() throws Exception {
        try {
            gameDAO.addGame("game");
            gameDAO.addGame("game2");
            gameDAO.addGame("game3");
            gameDAO.joinGame(2, null, "hey");
            gameDAO.joinGame(1, "1", "2");
            gameDAO.joinGame(4, "Hi", null);
            GameData game1 = gameDAO.getGame(1);
            GameData game2 = gameDAO.getGame(2);
            Assertions.assertEquals("1", game1.whiteUsername());
            Assertions.assertEquals("2", game1.blackUsername());
            Assertions.assertNull(game2.whiteUsername());
            Assertions.assertEquals("hey", game2.blackUsername());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: Game DNE", accessException.getMessage());
        }
    }
    @Test
    @Order(20)
    @DisplayName("Join bad game")
    public void joinBadColorTest() throws Exception {
        try {
            gameDAO.addGame("game");
            gameDAO.addGame("game2");
            gameDAO.addGame("game3");
            gameDAO.joinGame(1, "1", "2");
            gameDAO.joinGame(1, "3", "4");
            GameData game1 = gameDAO.getGame(1);
            GameData game2 = gameDAO.getGame(2);
            GameData game3 = gameDAO.getGame(3);
            Assertions.assertEquals("1", game1.whiteUsername());
            Assertions.assertEquals("2", game1.blackUsername());
            Assertions.assertEquals("Hi", game3.whiteUsername());
            Assertions.assertNull(game3.blackUsername());
            Assertions.assertNull(game2.whiteUsername());
            Assertions.assertEquals("hey", game2.blackUsername());
        }
        catch (DataAccessException accessException) {
            Assertions.assertEquals("Error: already taken", accessException.getMessage());
        }
    }
}
