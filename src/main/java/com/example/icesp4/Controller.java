package com.example.icesp4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;


public class Controller {

    private final AudioClip hoverSound = new AudioClip(getClass().getResource("Music/SoundEffects/appear-online.mp3").toExternalForm());

    @FXML
    protected void ChooseGame(){
        MainMenu.showChooseGameMenu();
    }

    @FXML
    protected void SettingsMenu() {
        MainMenu.showSettingsMenu();
    }

    @FXML
    protected void onExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void playHoverSound() {
        hoverSound.play();
    }
}
