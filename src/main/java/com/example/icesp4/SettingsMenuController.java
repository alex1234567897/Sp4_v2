package com.example.icesp4;

import com.example.icesp4.core.HighscoreManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsMenuController implements Initializable {

    @FXML
    private ChoiceBox <String> screenSizeBox;
    @FXML
    private Slider volumeSlider;

    @FXML
    private TextField ipField;

    @FXML
    private Label connectionStatusLabel;

    public HighscoreManager highscores;

    public void setHighscoreManager(HighscoreManager highscores) {
        this.highscores = highscores;

        if (ipField != null) {
            ipField.setText(highscores.getState().getServerBaseUrl());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screenSizeBox.getItems().addAll("768x672", "1024x896", "1280x1120");
        screenSizeBox.setValue("768x672");
        Platform.runLater(() -> screenSizeBox.setOnAction(this::ScreenSize));

        audioControl();
    }

    @FXML
    private void onTestConnection() {
        if (highscores == null) {
            connectionStatusLabel.setText("HighscoreManager not set (load ERROR!)");
            return;
        }

        String input = ipField.getText().trim();
        String baseUrl = normalizeBaseUrl(input);

        highscores.setServerBaseUrl(baseUrl);
        connectionStatusLabel.setText("Testing...");

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return highscores.testServer();
            }
        };

        task.setOnScheduled(e -> connectionStatusLabel.setText("OK" + task.getValue()));
        task.setOnFailed(e -> { Throwable ex = task.getException();
            connectionStatusLabel.setText("EROOR!" + (ex != null ? ex.getMessage() : "Unknown ERROR..."));
        });

        Thread t = new Thread(task, "server-test");
        t.setDaemon(true);
        t.start();
    }

    private String normalizeBaseUrl(String input) {

        if (input.isBlank()) {
            return "http://localhost:8080";
        }

        if (input.startsWith("http://") || input.startsWith("https://")) {
            return input;
        }

        return "http://" + input + ":8080";

    }

    @FXML
    protected void ScreenSize(ActionEvent event){
        Stage stage = null;

        if (event != null) {
            Node source = (Node) event.getSource();
            if (source.getScene() != null) {
                stage = (Stage) source.getScene().getWindow();
            }
        }

        if (stage == null) {
            // Scene not ready yet, ignore
            return;
        }

        String value = screenSizeBox.getValue();

        switch (value) {
            case "768x672" -> { stage.setWidth(672); stage.setHeight(768); }
            case "1024x896" -> { stage.setWidth(896); stage.setHeight(1024); }
            case "1280x1120" -> { stage.setWidth(1120); stage.setHeight(1280); }
            default -> { stage.setWidth(672); stage.setHeight(768); }
        }
    }

    @FXML
    protected void audioControl(){
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(Audio.getVolume());

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            Audio.setVolume(newVal.doubleValue());   // Updates global volume + MediaPlayer
        });

    }

    @FXML
    protected void returnTOMenu() {
        MainMenu.showMainMenu();
    }

    //sound effects
    private final AudioClip hoverSound = new AudioClip(getClass().getResource("Music/SoundEffects/appear-online.mp3").toExternalForm());
    @FXML
    private void playHoverSound() {
        hoverSound.play();
    }


}
