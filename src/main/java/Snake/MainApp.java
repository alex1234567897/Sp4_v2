package Snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showSnake();
        stage.show();
    }

    public static void showSnake() {
        SnakeGame game = new SnakeGame(600, 600);

        Scene scene = new Scene(game.getRoot());
        primaryStage.setScene(scene);

        game.start();
        game.attachInput(scene);
        }

        public static void main(String[] args) {
        launch(args);
    }
}
