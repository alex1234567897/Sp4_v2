package com.example.icesp4.core;

import com.example.icesp4.db.GlobalScore;
import com.example.icesp4.db.LocalDatabase;
import com.example.icesp4.net.HighscoreClient;

import java.util.List;

public class HighscoreManager {

    private final AppState state;
    private final LocalDatabase localDb;
    private final HighscoreClient client;

    public HighscoreManager(AppState state, LocalDatabase localDb, HighscoreClient client) {
        this.state = state;
        this.localDb = localDb;
        this.client = client;
    }

    public AppState getState() {
        return state;
    }

    public void setServerBaseUrl(String baseUrl) {
        state.setServerBaseUrl(baseUrl);
        client.setBaseUrl(baseUrl);
    }

    public String testServer() throws Exception {
        return client.testConnection();
    }

    public List<GlobalScore> fetchTop(String gameId, int limit) throws Exception {
        return client.getHighscores(gameId, limit);
    }

    public void saveLocal(String gameId, String name, int score) throws Exception {
        localDb.upsertLocalScore(gameId, name, score);
    }

    public boolean isHighEnoughForGlobal(String gameId, int score, int limit) throws Exception {
        List<GlobalScore> top = client.getHighscores(gameId, limit);
        if (top.size() < limit) return true;
        int worst = top.get(top.size() - 1).score;
        return score > worst;
    }

    public boolean submitGlobal(String gameId, String name, int score) {
        return client.submitScore(gameId, name, score);
    }
}