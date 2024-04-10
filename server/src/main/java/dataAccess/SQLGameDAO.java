package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    public GameData addGame (String gameName) throws DataAccessException {
        if (gameName == null) throw new DataAccessException("Error: no name");
        String chessGame = new Gson().toJson(new ChessGame());
        String statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        int id = executeUpdate(statement, null, null, gameName, chessGame);
        return getGame(id);
    }

    public Collection<GameData> listGame() throws DataAccessException {
        ArrayList<GameData> gameData = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameData.add(makeData(rs));
                    }
                }
            }
        }
        catch (DataAccessException | SQLException accessException) {
            throw new DataAccessException(accessException.getMessage());
        }
        return (gameData);
    }

    public GameData getGame(Integer id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM gameData WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return makeData(rs);
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        catch (SQLException accessException) {
            throw new DataAccessException(accessException.getMessage());
        }
    }

    public void joinGame(int id, String white, String black) throws DataAccessException {
        GameData game = getGame(id);
        if (game == null) throw new DataAccessException("Error: Game DNE");
        if (game.whiteUsername() != null && white != null) throw new ColorException("Error: already taken");
        if (game.blackUsername() != null && black != null) throw new ColorException("Error: already taken");
        if (white == null) white = game.whiteUsername();
        if (black == null) black = game.blackUsername();
        String statement = "UPDATE gameData set whiteUsername = ?, blackUsername = ? where id = ?";
        executeUpdate(statement, white, black, id);
    }

    public void updateGame(int id, ChessGame game) throws DataAccessException {
        String chessGame = new Gson().toJson(game);
        String statement = "UPDATE gameData SET game = ? where id = ?";
        executeUpdate(statement, chessGame, id);
    }
    public void leavePlayer(String auth, int id) throws DataAccessException {
        GameData check = getGame(id);
        String leave = new SQLAuthDAO().getAuth(auth).username();
        if (leave.equals(check.whiteUsername())) {
            String statement = "UPDATE gameData SET whiteUsername = null where id = ?";
            executeUpdate(statement, id);
        }
        else {
            String statement = "UPDATE gameData SET blackUsername = null where id = ?";
            executeUpdate(statement, id);
        }
    }

    public void clear() throws DataAccessException {
        String statement = "TRUNCATE gameData"; //think about using truncate users
        executeUpdate(statement);
    }

    private GameData makeData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame dbGame = new Gson().fromJson(game, ChessGame.class);
        return new GameData(id, whiteUsername, blackUsername, gameName, dbGame);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
                    //else if (param instanceof ChessGame p) ps.setString(i + 1, new Gson().toJson(p, ChessGame.class)); // p.toString());//TEST
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return -1;
    }

}
