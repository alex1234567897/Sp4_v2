package com.example.icesp4;

import SpaceInvaders.GameLoop;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.IntConsumer;

public final class SpaceInvadersRunner {

    private static final int W = 224;
    private static final int H = 256;
    private static final int SCALE = 3;

    private SpaceInvadersRunner() {}

    public static void startInStage(Stage stage, IntConsumer onGameExitWithScore) {
        Platform.runLater(() -> {
            GameLoop game = new GameLoop(W, H, SCALE, onGameExitWithScore);
            Scene scene = new Scene(game.getRoot(), W * SCALE, H * SCALE);
            game.attachInput(scene);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setWidth(W * SCALE);
            stage.setHeight(H * SCALE);
            stage.show();

            game.requestFocus();
            game.start();
        });
    }
}