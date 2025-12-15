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
        this.baseUrl = normalizeBaseUrl(baseUrl);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        System.out.println("Client baseUrl set to: " + this.baseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private String normalizeBaseUrl(String input) {
        String s = input == null ? "" : input.trim();

        if (s.isBlank()) return "http://localhost:8080";

        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);

        if (s.startsWith("http://") || s.startsWith("https://")) return s;

        if (s.matches("^[^:/]+:\\d+$")) return "http://" + s;

        return "http://" + s + ":8080";
    }

    private HttpURLConnection openGet(String endpoint) throws Exception {
        System.out.println("REQUEST URL = " + endpoint);

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        return conn;
    }

    private HttpURLConnection openPost(String endpoint) throws Exception {
        System.out.println("REQUEST URL = " + endpoint);

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        return conn;
    }

    public String testConnection() throws Exception {
        String endpoint = baseUrl + "/test";
        HttpURLConnection conn = openGet(endpoint);

        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } finally {
            conn.disconnect();
        }
    }

    public List<GlobalScore> getHighscores(String game, int limit) throws Exception {
        String endpoint = baseUrl + "/highscores?game=" +
                URLEncoder.encode(game, StandardCharsets.UTF_8) +
                "&limit=" + limit;
        System.out.println("baseUrl in client = " + baseUrl);
        System.out.println("REQUEST URL = " + endpoint);

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);

        try (InputStream is = conn.getInputStream()) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Type listType = new TypeToken<List<GlobalScore>>() {}.getType();
            return gson.fromJson(json, listType);
        } finally {
            conn.disconnect();
        }
    }

    public boolean submitScore(String game, String name, int score) {
        try {
            String endpoint = baseUrl + "/submit";
            HttpURLConnection conn = openPost(endpoint);

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
