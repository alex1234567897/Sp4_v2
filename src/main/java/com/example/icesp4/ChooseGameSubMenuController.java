package com.example.icesp4;

import SpaceInvaders.SpaceInvadersLauncher;
import SpaceInvaders.SpaceMain;
import com.example.icesp4.core.GameId;
import com.example.icesp4.core.HighscoreManager;
import com.example.icesp4.db.GlobalScore;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

import java.util.List;

public class ChooseGameSubMenuController {

    private HighscoreManager highscores;

    @FXML private VBox highscoreBox;
    @FXML private Label highscoreStatusLabel;

    private final AudioClip hoverSound =
            new AudioClip(getClass().getResource("Music/SoundEffects/appear-online.mp3").toExternalForm());

    @FXML
    private void playHoverSound() {
        hoverSound.play();
    }

    @FXML
    protected void returnToMenu() {
        MainMenu.showMainMenu();
    }

    @FXML
    protected void playGame() {
        MainMenu.showSpaceInvaders();
    }

    public void setHighscoreManager(HighscoreManager highscores) {
        this.highscores = highscores;

        System.out.println("ChooseGame got HighscoreManager. baseUrl = " +
                highscores.getState().getServerBaseUrl());

        loadTop10(GameId.SPACE_INVADERS);
    }

    private void loadTop10(String gameId) {
        if (highscores == null) {
            highscoreStatusLabel.setText("No HighscoreManager set");
            return;
        }

        highscoreStatusLabel.setText("Loading...");
        highscoreBox.getChildren().clear();

        Task<List<GlobalScore>> task = new Task<>() {
            @Override
            protected List<GlobalScore> call() throws Exception {
                System.out.println("Fetching highscores from: " + highscores.getState().getServerBaseUrl());
                return highscores.fetchTop(gameId, 10);
            }
        };

        task.setOnSucceeded(e -> {
            List<GlobalScore> scores = task.getValue();
            System.out.println("Highscores fetched: " + (scores == null ? 0 : scores.size()));
            showHighscores(scores);
        });

        task.setOnFailed(e -> {
            System.out.println("FAILED fetching highscores:");
            task.getException().printStackTrace();

            highscoreStatusLabel.setText("Failed " + task.getException().getMessage());
        });

        Thread t = new Thread(task, "fetch-highscores");
        t.setDaemon(true);
        t.start();
    }

    private void showHighscores(List<GlobalScore> scores) {
        highscoreBox.getChildren().clear();

        if (scores == null || scores.isEmpty()) {
            highscoreStatusLabel.setText("No highscores yet...");
            return;
        }

        int rank = 1;
        for (GlobalScore s : scores) {
            Label row = new Label(String.format("%2d. %s  %d", rank++, s.name, s.score));
            row.getStyleClass().add("highscoreRow");
            highscoreBox.getChildren().add(row);
        }

        highscoreStatusLabel.setText("");
    }
}
