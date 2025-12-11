package com.example.icesp4;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Audio {

    private static MediaPlayer bgPlayer;

    public static void playBackgroundMusic() {
        if (bgPlayer != null) {
            bgPlayer.stop();
        }

        Media media = new Media(Audio.class.getResource("Music/ChillPulse.mp3").toExternalForm());
        bgPlayer = new MediaPlayer(media);
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgPlayer.setVolume(0.4);
        bgPlayer.play();
    }
}
