package Snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Snake extends Application {

    private int width = 600;
    private int height = width;
    private int cellSize = 20;
    static List<Corner> snake = new ArrayList<>();
    private GraphicsContext gameFrame;
    static Dir direction = Dir.LEFT;
    Corner head;
    boolean gameOver = false;

    Pane game = new Pane();
    StackPane root = new StackPane(game);
    Scene scene = new Scene(root, width, 640);


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gameSetup(primaryStage);
        snakeSetup(gameFrame);
        controls();

    }

    public void gameSetup(Stage primaryStage) {


        //Her bliver canvas defineret som er det område i scenen spillet bliver vist
        game.setPrefSize(width, height);
        game.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        game.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Canvas canvas = new Canvas(width, height);
        gameFrame = canvas.getGraphicsContext2D();
        gameFrame.setFill(Color.LIGHTGRAY);
        game.getChildren().addAll(canvas);
        gameFrame.fillRect(0, 0, width, height);


        StackPane.setAlignment(game, Pos.CENTER);

        scene.setFill(Color.DARKGRAY);


        int cols = width / cellSize;
        int rows = height / cellSize;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {

                gameFrame.setStroke(Color.GRAY);
                gameFrame.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);


            }

        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void controls() {
        snakeSetup(gameFrame);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP)
                direction = Dir.UP;
            if (event.getCode() == KeyCode.DOWN)
                direction = Dir.DOWN;
            if (event.getCode() == KeyCode.LEFT)
                direction = Dir.LEFT;
            if (event.getCode() == KeyCode.RIGHT)
               direction = Dir.RIGHT;
        });

        switch (direction) {
            case UP:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case LEFT:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case RIGHT:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

        }
    }




    public void snakeSetup(GraphicsContext primaryStage) {

        snake.add(new Corner(20,20));
        snake.add(new Corner(20,20));
        snake.add(new Corner(20,20));

        head = snake.getFirst();
        gameFrame.setFill(Color.DARKGREEN);
        gameFrame.fillRect(head.getX() * cellSize, head.getY() * cellSize, cellSize, cellSize);

        gameFrame.setFill(Color.FORESTGREEN);
        for (int i = 1; i<snake.size(); i++) {
            Corner p = snake.get(i);
            gameFrame.fillRect(p.getX() * cellSize, p.getY() * cellSize, cellSize, cellSize);
        }

        root.getChildren().add((Node) snake);

    }

        /*
    public void updateSnake(){

        AnimationTimer snakeAnimation = new AnimationTimer();
            @Override
            public void handle(long now) {

            }

    }
*/
}


