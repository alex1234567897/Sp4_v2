package com.example.icesp4;

import com.example.icesp4.core.HighscoreManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;

public class HighscoreSubmitMenuController {

    @FXML private Label scoreLabel;
    @FXML private TextField nameField;
    @FXML private Label statusLabel;

    private HighscoreManager highscores;
    private String gameId;
    private int finalScore;

    public void init(HighscoreManager highscores, String gameId, int finalScore) {
        this.highscores = highscores;
        this.gameId = gameId;
        this.finalScore = finalScore;

        scoreLabel.setText("SCORE: " + finalScore);

        nameField.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) return;
            String up = newV.toUpperCase().replaceAll("[^A-Z0-9]", "");
            if (up.length() > 3) up = up.substring(0, 3);
            if (!up.equals(newV)) nameField.setText(up);
        });

        nameField.setText("AAA");
        statusLabel.setText("");
    }

    @FXML
    private void onSkip() {
        MainMenu.showMainMenu();
    }

    @FXML
    private void onSave() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim().toUpperCase();
        if (name.length() != 3) {
            statusLabel.setText("Name must be 3 characters");
            return;
        }

        statusLabel.setText("Saving...");

        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                highscores.saveLocal(gameId, name, finalScore);

                if (highscores.isHighEnoughForGlobal(gameId, finalScore, 10)) {
                    highscores.submitGlobal(gameId, name, finalScore);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> MainMenu.showMainMenu());
        task.setOnFailed(e -> statusLabel.setText("ERROR: " + task.getException().getMessage()));

        Thread t = new Thread(task, "save-highscore");
        t.setDaemon(true);
        t.start();
    }

    //sound effects
    private final AudioClip hoverSound = new AudioClip(getClass().getResource("Music/SoundEffects/appear-online.mp3").toExternalForm());
    @FXML
    private void playHoverSound() {
        hoverSound.play();
    }


}
