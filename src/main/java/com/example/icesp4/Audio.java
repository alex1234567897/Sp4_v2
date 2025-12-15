package com.example.icesp4;

import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Audio {

    private static MediaPlayer bgPlayer;

    public static double getVolume() {
        return volume;
    }

    public static void setVolume(double volume) {
        Audio.volume = volume;
        if (bgPlayer != null) {
            bgPlayer.setVolume(volume);
        }
    }

    private static double volume = 0.5;

    public static void playBackgroundMusic() {
        if (bgPlayer != null) {
            bgPlayer.stop();
        }

        Media media = new Media(Audio.class.getResource("Music/retroArcade.mp3").toExternalForm());
        bgPlayer = new MediaPlayer(media);
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgPlayer.setVolume(volume);
        bgPlayer.play();
    }

}
