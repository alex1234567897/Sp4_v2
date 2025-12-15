package SpaceInvaders;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SpaceInvadersLauncher {

    private static final int W = 224;
    private static final int H = 256;
    private static final int SCALE = 3;

    private SpaceInvadersLauncher() {}

    public static void openInNewWindow() {
        Platform.runLater(() -> {
            Stage stage = new Stage();

            GameLoop game = new GameLoop(W, H, SCALE, score -> {});
            Scene scene = new Scene(game.getRoot(), W * SCALE, H * SCALE);
            game.attachInput(scene);

            stage.setTitle("Space Invaders");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            game.requestFocus();
            game.start();
        });
    }
}