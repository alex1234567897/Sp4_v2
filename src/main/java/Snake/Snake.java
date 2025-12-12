package Snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private List<Point> snake = new ArrayList<>();
    private GraphicsContext gameFrame;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gameSetup(primaryStage);
        snakeSetup(gameFrame);

    }

    public void gameSetup(Stage primaryStage) {

        Pane game = new Pane();
        //Her bliver canvas defineret som er det område i scenen spillet bliver vist
        game.setPrefSize(width, height);
        game.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        game.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Canvas canvas = new Canvas(width, height);
        gameFrame = canvas.getGraphicsContext2D();
        gameFrame.setFill(Color.LIGHTGRAY);
        game.getChildren().add(canvas);
        gameFrame.fillRect(0, 0, width, height);

        StackPane root = new StackPane(game);
        StackPane.setAlignment(game, Pos.CENTER);
        Scene scene = new Scene(root, width, 640);
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

    public void snakeSetup(GraphicsContext gameFrame) {

        snake.add(new Point(15, 15));
        snake.add(new Point(14, 15));
        snake.add(new Point(13, 15));

        Point head = snake.getFirst();
        gameFrame.setFill(Color.DARKGREEN);
        gameFrame.fillRect(head.x * cellSize, head.y * cellSize, cellSize, cellSize);

        gameFrame.setFill(Color.FORESTGREEN);
        for (int i = 1; i<snake.size(); i++) {
            Point p = snake.get(i);
            gameFrame.fillRect(p.x * cellSize, p.y * cellSize, cellSize, cellSize);
        }

    }

    public void updateSnake(){

    }

    /*public void snakeMovement() {

        AnimationTimer snakeAnimation = new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        }


        }*/


    }

