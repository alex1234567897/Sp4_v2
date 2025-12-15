package com.example.icesp4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Controller {

    @FXML 
    protected void SettingsMenu() {
        MainMenu.showSettingsMenu();
    }

    @FXML
    protected void ChooseGame(){

    }

    @FXML
    protected void onExitButtonClick() {
        Platform.exit();
    }

}
