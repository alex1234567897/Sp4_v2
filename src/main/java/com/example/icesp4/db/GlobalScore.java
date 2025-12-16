package com.example.icesp4.db;

public class GlobalScore {
    public String game;
    public String name;
    public int score;
    public String createdAt;

    public GlobalScore() {}

    public GlobalScore(String game, String name, int score, String createdAt) {
        this.game = game;
        this.name = name;
        this.score = score;
        this.createdAt = createdAt;
    }
}