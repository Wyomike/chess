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

    public GameData addGame (String gameName) throws DataAccessException { //TODO - serialize games?
        String chessGame = new Gson().toJson(new ChessGame());
        String statement = "INSERT INTO gameData (NULL, NULL, gameName, chessGame) VALUES (?, ?)";
        int id = executeUpdate(statement, gameName, chessGame);
        return getGame(id);
    }
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
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
        if (game.whiteUsername() != null && white != null) throw new ColorException("Error: already taken");
        if (game.blackUsername() != null && black != null) throw new ColorException("Error: already taken");
        String statement = "UPDATE gameData set whiteUsername = ?, blackUsername = ? where id = ?; VALUES (?, ?, ?)";
        executeUpdate(statement, white, black, id);
    }

    public void deleteGameData(int id)  throws DataAccessException {
        String statement = "DELETE FROM gameData WHERE id = ?";
        executeUpdate(statement, id);
    }

    public void clear() throws DataAccessException {
        String statement = "DELETE FROM gameData"; //think about using truncate users
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
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return -1;
    }

}
