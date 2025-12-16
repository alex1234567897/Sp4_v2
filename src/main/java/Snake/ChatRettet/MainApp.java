package Snake.ChatRettet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Koden er rettet af Chatgpt så vi kunne få den til at køre inde i Menuen
 **/

/*public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showSnake();
        stage.setTitle("Snake Test");
        stage.show();
    }

    public static void showSnake() {
        final int width = 600;
        final int height = 600;
        SnakeGame game = new SnakeGame(width, height);

        // Optional: set a listener for highscore submission
        game.setListener(score -> System.out.println("Game Over! Score: " + score));

        // Scene uses SnakeGame directly
        Scene scene = new Scene(game, width, height);
        primaryStage.setScene(scene);

        // Attach input and start game
        game.attachInput(scene);
        game.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/