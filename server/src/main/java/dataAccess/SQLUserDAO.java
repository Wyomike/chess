package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public UserData addUser(String username, String password, String email) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        UserData data = new UserData(username, hashedPassword, email);

        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        //String json = new Gson().toJson(data);
        executeUpdate(statement, username, hashedPassword, email);
        return data;
    }
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    public Collection<UserData> listUsers() throws DataAccessException {
        ArrayList<UserData> userData = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        userData.add(makeData(rs));
                    }
                }
            }
        }
        catch (DataAccessException | SQLException accessException) {
            throw new DataAccessException(accessException.getMessage());
        }
        return (userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
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
        //return new UserData(username, "a", "a");
    }

    public boolean getLogin(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //makeData(rs);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException accessException) {
            throw new DataAccessException(accessException.getMessage());
        }
        //return false;
    }

    public void deleteUserData(String username)  throws DataAccessException {
        String statement = "DELETE FROM users WHERE username = ?";
        executeUpdate(statement, username);
    }

    public void clear()  throws DataAccessException {
        String statement = "TRUNCATE users"; //think about using truncate users
        executeUpdate(statement);
    }

    private UserData makeData(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
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
