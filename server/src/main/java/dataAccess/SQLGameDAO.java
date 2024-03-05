package dataAccess;

import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    public GameData addGame (int id) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        GameData data = new GameData(token, username);

        String statement = "INSERT INTO authData (token, username) VALUES (?, ?)";
        executeUpdate(statement, token, username);

        return data;
    }
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    public Collection<GameData> listGame() throws DataAccessException {
        ArrayList<GameData> authData = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT token, username FROM authData";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        authData.add(makeData(rs));
                    }
                }
            }
        }
        catch (DataAccessException | SQLException accessException) {
            throw new DataAccessException(accessException.getMessage());
        }
        return (authData);
    }

    public GameData getGame(String id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM authData WHERE token=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String token = rs.getString("token");
                        String username = rs.getString("username");
                        return new GameData(token, username);
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

    public void deleteGameData(String id)  throws DataAccessException {
        String statement = "DELETE FROM authData WHERE token = ?";
        executeUpdate(statement, id);
    }

    public void clear()  throws DataAccessException {
        String statement = "DELETE FROM authData"; //think about using truncate users
        executeUpdate(statement);
    }

    private GameData makeData(ResultSet rs) throws SQLException {
        String token = UUID.randomUUID().toString();
        String username = rs.getString("username");
        return new GameData(token, username);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
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

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

}
