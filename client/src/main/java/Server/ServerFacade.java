package Server;

import com.google.gson.Gson;
import model.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;
    private final HttpHandler httpHandler;

    public ServerFacade(int url) {
        String urlHead = "http://localhost:";
        urlHead += String.valueOf(url);
        serverUrl = urlHead;
        httpHandler = new HttpHandler(serverUrl);
    }
    public AuthData register(String username, String password, String email) throws ResponseException {
        UserData userData = new UserData(username, password, email);
        try {
            return httpHandler.register(userData);
        }
        catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            return null;
        }

//        HttpURLConnection connection = prepConnection("POST", "/user");
//
//        try(OutputStream requestBody = connection.getOutputStream()) {
//            // Write request body to OutputStream ...
//            String reqData = new Gson().toJson(userData);
//            requestBody.write(reqData.getBytes());
//        }
//        catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            // Get HTTP response headers, if necessary
//            // Map<String, List<String>> headers = connection.getHeaderFields();
//            // OR
//            //connection.getHeaderField("Content-Length");
//
//            InputStream responseBody = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            AuthData response = new Gson().fromJson(reader, AuthData.class);
//            connection.disconnect();
//            return response;
//        }
//        else {
//            // SERVER RETURNED AN HTTP ERROR
//            InputStream responseBody = connection.getErrorStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            String error = reader.toString();
//            throw new ResponseException(error);
//        }
    }

    public AuthData login(String username, String password) throws ResponseException, IOException {
        LoginRequest login = new LoginRequest(username, password);
//        try {
            return httpHandler.login(login);
//        }
//        catch (IOException ioException) {
//            System.out.println(ioException.getMessage());
//            return null;
//        }

//        HttpURLConnection connection = prepConnection("POST", "/session");
//
//        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
//            String reqData = new Gson().toJson(login);
//            requestBody.write(reqData.getBytes());
//        }
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
//            InputStream responseBody = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            AuthData response = new Gson().fromJson(reader, AuthData.class);
//            return response;
//        }
//        else { // SERVER RETURNED AN HTTP ERROR
//            InputStream responseBody = connection.getErrorStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            String error = reader.toString();
//            throw new ResponseException(error);
//        }
    }

    public void logout(String authToken) throws ResponseException, IOException {
        //try {
            httpHandler.logout(authToken);
        //}
        //catch (IOException ioException) {
        //    System.out.println(ioException.getMessage());
        //}
    }

    public Object listGames(String authToken) throws ResponseException, IOException {
//        try {
            return httpHandler.listGames(authToken);
//        }
//        catch (IOException ioException) {
//            System.out.println(ioException.getMessage());
//            return null;
//        }
//        HttpURLConnection connection = prepConnection("GET", "/game");
//        connection.setRequestProperty("Authorization", authToken);
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
//            InputStream responseBody = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            Object response = new Gson().fromJson(reader, Object.class);//POSSIBLY AN ERROR
//            connection.disconnect();
//            return response;
//        }
//        else { //handle input
//            connection.setRequestProperty("Authorization", authToken);
//            InputStream responseBody = connection.getErrorStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            String error = reader.toString();
//            throw new ResponseException(error);
//        }
    }

    public GameData createGame(String gameName, String authToken) throws ResponseException, IOException {
//        try {
            return httpHandler.createGame(gameName, authToken);
//        }
//        catch (IOException ioException) {
//            System.out.println(ioException.getMessage());
//            return null;
//        }
//        HttpURLConnection connection = prepConnection("POST", "/game");
//
//        connection.setRequestProperty("Authorization", authToken);
//        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
//            String reqData = new Gson().toJson(gameName);
//            requestBody.write(reqData.getBytes());
//        }
//        catch (Exception ioException) {
//            System.out.print("eEe" + ioException.getMessage());
//        }
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
//            InputStream responseBody = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            GameData response = new Gson().fromJson(reader, GameData.class);
//            return response;
//        }
//        else { // SERVER RETURNED AN HTTP ERROR
//            InputStream responseBody = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            String error = new Gson().fromJson(reader, String.class);
//            throw new ResponseException(error);
//        }
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws ResponseException, IOException {
//        try {
            httpHandler.joinGame(playerColor, gameID, authToken);
//        }
//        catch (IOException ioException) {
//            System.out.println(ioException.getMessage());
//        }
//        HttpURLConnection connection = prepConnection("PUT", "/game");
//
//        connection.setRequestProperty("Authorization", authToken);
//        try(OutputStream requestBody = connection.getOutputStream()) { //handle output
//            String reqData = new Gson().toJson(new JoinRequest(playerColor, gameID));
//            requestBody.write(reqData.getBytes());
//        }
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //handle input
////            InputStream responseBody = connection.getInputStream();
////            InputStreamReader reader = new InputStreamReader(responseBody);
////            GameData response = new Gson().fromJson(reader, GameData.class);
////            return response;
//        }
//        else { // SERVER RETURNED AN HTTP ERROR
//            InputStream responseBody = connection.getErrorStream();
//            InputStreamReader reader = new InputStreamReader(responseBody);
//            String error = reader.toString();
//            throw new ResponseException(error);
//        }
    }

    public void clear() throws ResponseException {
        try {
            httpHandler.clear();
        }
        catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    private HttpURLConnection prepConnection(String method, String path) throws IOException {
        URL url = new URL(serverUrl + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        return connection;
    } //
//    private HttpURLConnection prepConnection(String method, String path, String authToken) throws IOException {
//        URL url = new URL(serverUrl + path);
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", authToken);
//        connection.setReadTimeout(5000);
//        connection.setRequestMethod(method);
//        connection.setDoOutput(true);
//        return connection;
//    }
}
