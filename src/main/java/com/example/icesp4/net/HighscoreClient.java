package com.example.icesp4.net;

import com.example.icesp4.db.GlobalScore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HighscoreClient {

    private String baseUrl;
    private final Gson gson = new Gson();

    public HighscoreClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String testConnection() throws Exception {
        URL url = new URL(baseUrl + "/test");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream is = conn.getInputStream()) {
            String result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            conn.disconnect();
            return result;
        }
    }

    public List<GlobalScore> getHighscores(String game, int limit) throws Exception {
        String endpoint = baseUrl + "/highscores?game=" +
                URLEncoder.encode(game, StandardCharsets.UTF_8) +
                "&limit=" + limit;

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream is = conn.getInputStream()) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            conn.disconnect();

            Type listType = new TypeToken<List<GlobalScore>>() {}.getType();
            return gson.fromJson(json, listType);
        }
    }

    public boolean submitScore(String game, String name, int score) {
        try {
            URL url = new URL(baseUrl + "/submit");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            SubmitRequest req = new SubmitRequest(game, name.toUpperCase(), score);
            String json = gson.toJson(req);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            conn.disconnect();
            return code == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class SubmitRequest {
        String game;
        String name;
        int score;

        SubmitRequest(String game, String name, int score) {
            this.game = game;
            this.name = name;
            this.score = score;
        }
    }
}
