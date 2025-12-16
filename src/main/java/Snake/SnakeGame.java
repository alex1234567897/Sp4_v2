package Snake;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame {

    // === KONFIGURATION ===
    private final int width;
    private final int height;
    private final int cellSize = 20;

    // === GAME STATE ===
    private boolean gameOver = false;
    private double speed = 6.5;
    private int points = 0;

    private int cols;
    private int rows;

    private static final Random rand = new Random();

    private final List<Corner> snake = new ArrayList<>();
    private int foodX;
    private int foodY;
    private int foodColor;

    private Dir direction = Dir.LEFT;
    private Dir nextDirection = Dir.LEFT;

    // === JAVA FX ===
    private final Pane gamePane = new Pane();
    private final StackPane root = new StackPane(gamePane);
    private final Canvas canvas;
    private final GraphicsContext gc;

    private AnimationTimer timer;

    // === KONSTRUKTØR ===
    public SnakeGame(int width, int height) {
        this.width = width;
        this.height = height;

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        gamePane.getChildren().add(canvas);

        cols = width / cellSize;
        rows = height / cellSize;

        drawGrid();
        pressToStart();
    }

    // === PUBLIC API ===
    public Parent getRoot() {
        return root;
    }

    public int getPoints() {
        return points;
    }

    public void attachInput(Scene scene) {
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
    }

    public void start() {
        timer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    update();
                    return;
                }

                if (now - lastTick > 1_000_000_000 / speed) {
                    lastTick = now;
                    update();
                }
            }
        };
        timer.start();
    }

    // === GAME LOGIC ===
    private void pressToStart() {
        gc.setFill(Color.FORESTGREEN);
        gc.setFont(new Font(40));
        gc.fillText("Press any key to start", 60, height / 2);

        root.setOnKeyPressed(e -> {
            root.setOnKeyPressed(null);
            initGame();
            start();
        });
    }

    private void initGame() {
        snake.clear();
        snake.add(new Corner(10, 10));
        snake.add(new Corner(9, 10));
        snake.add(new Corner(8, 10));

        direction = Dir.RIGHT;
        nextDirection = Dir.RIGHT;
        gameOver = false;

        newFood();
    }

    private void update() {
        if (gameOver) {
            drawGameOver();
            timer.stop();
            return;
        }

        direction = nextDirection;

        moveSnake();
        checkCollisions();

        draw();
    }

    private void moveSnake() {
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        Corner head = snake.get(0);
        switch (direction) {
            case UP -> head.y--;
            case DOWN -> head.y++;
            case LEFT -> head.x--;
            case RIGHT -> head.x++;
        }
    }

    private void checkCollisions() {
        Corner head = snake.get(0);

        if (head.x < 0 || head.y < 0 || head.x >= cols || head.y >= rows) {
            gameOver = true;
            points = snake.size() - 3;
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.x == snake.get(i).x && head.y == snake.get(i).y) {
                gameOver = true;
                points = snake.size() - 3;
                return;
            }
        }

        if (head.x == foodX && head.y == foodY) {
            snake.add(new Corner(-1, -1));
            newFood();
        }
    }

    // === DRAWING ===
    private void draw() {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        drawGrid();

        // food
        gc.setFill(getFoodColor());
        gc.fillRect(foodX * cellSize, foodY * cellSize, cellSize - 1, cellSize - 1);

        // snake
        for (int i = 0; i < snake.size(); i++) {
            gc.setFill(i == 0 ? Color.DARKGREEN : Color.FORESTGREEN);
            Corner c = snake.get(i);
            gc.fillRect(c.x * cellSize, c.y * cellSize, cellSize - 1, cellSize - 1);
        }
    }

    private void drawGrid() {
        gc.setStroke(Color.GRAY);
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawGameOver() {
        gc.setFill(Color.RED);
        gc.setFont(new Font(50));
        gc.fillText("GAME OVER", 120, height / 2);
        gc.fillText("Points: " + points, 160, height / 2 + 60);
    }

    private void newFood() {
        while (true) {
            foodX = rand.nextInt(cols);
            foodY = rand.nextInt(rows);

            boolean onSnake = snake.stream()
                    .anyMatch(c -> c.x == foodX && c.y == foodY);

            if (!onSnake) {
                foodColor = rand.nextInt(5);
                return;
            }
        }
    }

    private Color getFoodColor() {
        return switch (foodColor) {
            case 0 -> Color.RED;
            case 1 -> Color.ORANGE;
            case 2 -> Color.YELLOW;
            case 3 -> Color.PURPLE;
            default -> Color.PINK;
        };
    }
}
