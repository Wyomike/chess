package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpHandler {
    String serverUrl;
    public HttpHandler(String url) {
        serverUrl = url;
    }

    public AuthData register(UserData userData) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("POST", "/user");

        try(OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            String reqData = new Gson().toJson(userData);
            requestBody.write(reqData.getBytes());
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            AuthData response = new Gson().fromJson(reader, AuthData.class);
            connection.disconnect();
            return response;
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    public AuthData login(LoginRequest login) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("POST", "/session");

        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
            String reqData = new Gson().toJson(login);
            requestBody.write(reqData.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            AuthData response = new Gson().fromJson(reader, AuthData.class);
            return response;
        }
        else { // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    public void logout(String authToken) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("DELETE", "/session");
        connection.setRequestProperty("Authorization", authToken);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) { //handle input
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("GET", "/game");
        connection.setRequestProperty("Authorization", authToken);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            //Type listType = new TypeToken<ArrayList<GameData>>(){}.getType();
            Type mapType = new TypeToken<Map<String, ArrayList<GameData>>>(){}.getType();
            Map<String, ArrayList<GameData>> response = new Gson().fromJson(reader, mapType);
//            JsonObject jsonObject = JsonParser.parseString(reader.toString()).getAsJsonObject();
//            JsonArray jsonArray = jsonObject.getAsJsonArray("games");
//            ArrayList<GameData> response = new Gson().fromJson(jsonArray, listType);
            connection.disconnect();
            return response.get("games");
        }
        else { //handle input
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    public GameData createGame(String gameName, String authToken) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("POST", "/game");

        connection.setRequestProperty("Authorization", authToken);
        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
            String reqData = new Gson().toJson(new GameData(0, null, null, gameName, null));
            requestBody.write(reqData.getBytes());
        }
        catch (Exception ioException) {
            System.out.print("eEe" + ioException.getMessage());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            GameData response = new Gson().fromJson(reader, GameData.class);
            return response;
        }
        else { // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = new Gson().fromJson(reader, String.class);
            throw new ResponseException(error);
        }
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("PUT", "/game");

        connection.setRequestProperty("Authorization", authToken);
        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
            String reqData = new Gson().toJson(new JoinRequest(playerColor, gameID));
            requestBody.write(reqData.getBytes());
        }

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) { // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    public void clear() throws IOException, ResponseException {
        HttpURLConnection connection = prepConnection("DELETE", "/db");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) { //handle input
            InputStream responseBody = connection.getErrorStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            String error = reader.toString();
            throw new ResponseException(error);
        }
    }

    private HttpURLConnection prepConnection(String method, String path) throws IOException {
        URL url = new URL(serverUrl + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        return connection;
    }
}
