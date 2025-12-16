package com.example.icesp4.db;

public class LocalScore {
    public final String game;
    public final String name;
    public final int score;
    public final String updatedAt;

    public LocalScore(String game, String name, int score, String updatedAt) {
        this.game = game;
        this.name = name;
        this.score = score;
        this.updatedAt = updatedAt;
    }
}
