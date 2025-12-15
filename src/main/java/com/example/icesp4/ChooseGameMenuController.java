package com.example.icesp4;

import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;

public class ChooseGameMenuController {

    @FXML
    protected void returnToMenu() {
        MainMenu.showMainMenu();
    }

    @FXML
    protected void SpaceInvaders(){
        MainMenu.showChooseGameSubMenu();

    }

    //sound effects
    private final AudioClip hoverSound = new AudioClip(getClass().getResource("Music/SoundEffects/appear-online.mp3").toExternalForm());
    @FXML
    private void playHoverSound() {
        hoverSound.play();
    }



}
