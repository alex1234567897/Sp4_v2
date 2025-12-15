package SpaceInvaders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int W = 224;
    private static final int H = 256;
    private static final int SCALE = 3;

    @Override
    public void start(Stage stage) {
        GameLoop game = new GameLoop(W, H, SCALE);

        Scene scene = new Scene(game.getRoot(), W * SCALE, H * SCALE);
        game.attachInput(scene);

        stage.setTitle("Space Invaders");
        stage.setScene(scene);
        stage.show();

        game.requestFocus();
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
