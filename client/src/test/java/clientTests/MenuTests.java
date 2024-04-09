package clientTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;
import service.GameService;
import service.UserService;

public class MenuTests {
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
    @DisplayName("Test games")
    public void clearTest() throws Exception {
        AuthData authTest = authDAO.addAuth(testUser.username());
        AuthData authNew = authDAO.addAuth(newTestUser.username());
        gameService.createGame("game", authTest.authToken());
        gameService.joinGame("WHITE", 1, authTest.authToken());
        gameService.joinGame("BLACK", 1, authNew.authToken());

    }
}
