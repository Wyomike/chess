package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {



    public UserData addUser(String username, String password, String email) throws DataAccessException {
        UserData data = new UserData(username, password, email);

        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String json = new Gson().toJson(data);
        executeUpdate(statement, username, password, email);
        return data;
//        UserData data = new UserData(username, password, email);
//        userData.put(username, data);
//        return data;
//
//        var statement = "INSERT INTO pet (name, type, json) VALUES (?, ?, ?)";
//        var json = new Gson().toJson(pet);
//        var id = executeUpdate(statement, pet.name(), pet.type(), json);
//        return new Pet(id, pet.name(), pet.type());
    }
    //I just got rid of response exceptions, we'll find out later if we need them. If need, reference petshop.
    public Collection<UserData> listUsers() {
        HashMap<String, UserData> userData = new HashMap<>();
        return (userData.values());
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
//    public Pet getPet(int id) throws ResponseException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT id, json FROM pet WHERE id=?";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setInt(1, id);
//                try (var rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        return readPet(rs);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
//        }
//        return null;
//    }


    public boolean getLogin(String username, String password) {
        return false;
    }

    public void deleteUserData(String username) {

    }

    public void clear() {

    }

    private UserData makeData(ResultSet rs) throws SQLException {
        System.out.println(rs.toString());
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

//    public void example() throws Exception {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
//                var rs = preparedStatement.executeQuery();
//                rs.next();
//                System.out.println(rs.getInt(1));
//            }
//        }
//    }

}
