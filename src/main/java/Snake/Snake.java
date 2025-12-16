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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;


import java.util.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class Snake extends Application {

    private int width = 600;
    private int height = width;
    static int foodcolor = 0;
    private int cellSize = 20;
    static int foodX = 0;
    static int foodY = 0;
    static List<Corner> snake = new ArrayList<>();
    private GraphicsContext gameFrame;
    static Dir direction = Dir.LEFT;
    static Dir nextDirection = Dir.LEFT;
    static Random rand = new Random();
    Corner head;
    boolean gameOver = false;
    private double speed = 6.5;
    private int points = 0;

    private int cols;
    private int rows;


    Pane game = new Pane();
    StackPane root = new StackPane(game);
    Canvas canvas = new Canvas(width, height);
    Scene scene = new Scene(root, width, 640);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gameSetup();

        pressToStart();

        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void gameSetup() {

        game.setPrefSize(width, height);
        game.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        game.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        game.getChildren().addAll(canvas);
        gameFrame = canvas.getGraphicsContext2D();
        //gameFrame.setFill(Color.LIGHTGRAY);
        //gameFrame.fillRect(0, 0, width, height);
        //StackPane.setAlignment(game, Pos.CENTER);

        cols = width / cellSize;
        rows = height / cellSize;


        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {

                gameFrame.setStroke(Color.GRAY);
                gameFrame.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);

            }
        }
    }

    public void pressToStart() {
        gameFrame.setFill(Color.FORESTGREEN);
        gameFrame.setFont(new Font("", 50));
        gameFrame.fillText("Press any key to start", 70, 300);

        scene.setOnKeyPressed(event -> {
            // if (event.getCode() == KeyCode.SPACE)
            newFood();
            animationTimer();
            snakeSetup();


        });
    }

    public void animationTimer() {
        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    updateSnake(gameFrame);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    updateSnake(gameFrame);
                }
            }

        }.start();

    }

    public void snakeSetup() {
        scene.setFill(Color.DARKGRAY);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP && direction != Dir.DOWN)
                nextDirection = Dir.UP;
            if (event.getCode() == KeyCode.DOWN && direction != Dir.UP)
                nextDirection = Dir.DOWN;
            if (event.getCode() == KeyCode.LEFT && direction != Dir.RIGHT)
                nextDirection = Dir.LEFT;
            if (event.getCode() == KeyCode.RIGHT && direction != Dir.LEFT)
                nextDirection = Dir.RIGHT;
        });

        snake.add(new Corner(20, 5));
        snake.add(new Corner(20, 5));
        snake.add(new Corner(20, 5));

    }

    public void updateSnake(GraphicsContext gameFrame) {
        if (gameOver) {
            gameFrame.setFill(Color.RED);
            gameFrame.setFont(new Font("", 50));
            gameFrame.fillText("GAME OVER", 150, 300);
            points = snake.size() - 3;
            gameFrame.fillText("Points: " +
                    points, 175, 500);

            return;
        }


        direction = nextDirection;

        gameFrame.setFill(Color.LIGHTGRAY);
        gameFrame.fillRect(0, 0, width, height);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {

                gameFrame.setStroke(Color.GRAY);
                gameFrame.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);

            }
        }

        Color foodColor;
        switch (foodcolor) {
            case 0:
                foodColor = Color.RED;
                break;
            case 1:
                foodColor = Color.ORANGE;
                break;
            case 2:
                foodColor = Color.YELLOW;
                break;
            case 3:
                foodColor = Color.PURPLE;
                break;
            default:
                foodColor = Color.PINK;
        }
        gameFrame.setFill(foodColor);
        gameFrame.fillRect(foodX * cellSize, foodY * cellSize, cellSize - 1, cellSize - 1);

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        switch (direction) {

            case UP:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y >= rows) {
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
                if (snake.get(0).x >= cols) {
                    gameOver = true;
                }
                break;

        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }


        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }
        Color cc = Color.RED;
        head = snake.getFirst();
        gameFrame.setFill(Color.DARKGREEN);
        gameFrame.fillRect(head.x * cellSize, head.y * cellSize, cellSize - 1, cellSize - 1);

        for (int i = 1; i < snake.size(); i++) {
            Corner p = snake.get(i);
            gameFrame.setFill(Color.FORESTGREEN);
            gameFrame.fillRect(p.x * cellSize, p.y * cellSize, cellSize - 1, cellSize - 1);

        }
    }

    public static void newFood() {

        start:
        while (true) {

            foodX = rand.nextInt(20);
            foodY = rand.nextInt(20);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            break;

        }
    }

    public int getPoints() {
        return points;
    }
}