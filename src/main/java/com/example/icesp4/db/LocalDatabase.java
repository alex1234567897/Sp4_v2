package com.example.icesp4.db;

import java.sql.*;

public class LocalDatabase {

    private final String url;

    public LocalDatabase(String fileName) {
        this.url = "jdbc:sqlite:" + fileName;
    }

    public void init() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS local_scores (
                    game       TEXT NOT NULL,
                    name       TEXT NOT NULL CHECK (length(name) = 3),
                    score      INTEGER NOT NULL,
                    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (game, name)
                )
                """);
        }
    }

    public LocalScore getLocalScore(String game, String name) throws SQLException {
        String sql = """
            SELECT game, name, score, updated_at
            FROM local_scores
            WHERE game = ? AND name = ?
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, game);
            ps.setString(2, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LocalScore(
                            rs.getString("game"),
                            rs.getString("name"),
                            rs.getInt("score"),
                            rs.getString("updated_at")
                    );
                }
                return null;
            }
        }
    }

    public void upsertLocalScore(String game, String name, int score) throws SQLException {
        String sql = """
            INSERT INTO local_scores (game, name, score, updated_at)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT(game, name) DO UPDATE SET
                score = excluded.score,
                updated_at = CURRENT_TIMESTAMP
            WHERE excluded.score > local_scores.score
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, game);
            ps.setString(2, name.toUpperCase());
            ps.setInt(3, score);
            ps.executeUpdate();
        }
    }
}
