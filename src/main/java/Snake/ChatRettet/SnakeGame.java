package Snake.ChatRettet;


/**
 * Koden er rettet af Chatgpt så vi kunne få den til at køre inde i Menuen
 */

/*public class SnakeGame extends Pane {

    private final int width;
    private final int height;
    private final int cellSize = 20;
    private final int cols;
    private final int rows;

    private Canvas canvas;
    private GraphicsContext gc;

    private List<Corner> snake = new ArrayList<>();
    private Dir direction = Dir.LEFT;
    private Dir nextDirection = Dir.LEFT;
    private int foodX, foodY;
    private int foodColor;
    private Random rand = new Random();
    private boolean gameOver = false;

    private double speed = 6.5;
    private AnimationTimer timer;

    private SnakeGameListener listener; // callback for highscore submission

    public SnakeGame(int width, int height) {
        this.width = width;
        this.height = height;
        this.cols = width / cellSize;
        this.rows = height / cellSize;

        setPrefSize(width, height);

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);;
    }


    public interface SnakeGameListener {
        void onGameOver(int score);
    }

    public void setListener(SnakeGameListener listener) {
        this.listener = listener;
    }


    public void startGame() {
        gameOver = false;
        snake.clear();
        snake.add(new Corner(cols / 2, rows / 2));
        snake.add(new Corner(cols / 2 + 1, rows / 2));
        snake.add(new Corner(cols / 2 + 2, rows / 2));

        direction = Dir.LEFT;
        nextDirection = Dir.LEFT;

        spawnFood();

        timer = new AnimationTimer() {
            private long lastTick = 0;

            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    render();
                    return;
                }

                if (now - lastTick > 1_000_000_000 / speed) {
                    lastTick = now;
                    update();
                    render();
                }
            }
        };
        timer.start();
    }


    public void stopGame() {
        if (timer != null) {
            timer.stop();
        }
    }


    public void attachInput(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP -> { if (direction != Dir.DOWN) nextDirection = Dir.UP; }
                case DOWN -> { if (direction != Dir.UP) nextDirection = Dir.DOWN; }
                case LEFT -> { if (direction != Dir.RIGHT) nextDirection = Dir.LEFT; }
                case RIGHT -> { if (direction != Dir.LEFT) nextDirection = Dir.RIGHT; }
            }
        });
    }


    private void update() {
        if (gameOver) return;

        direction = nextDirection;

        // Move snake
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

        // Check collisions with walls
        if (head.x < 0 || head.y < 0 || head.x >= cols || head.y >= rows) {
            gameOver = true;
            stopGame();
            if (listener != null) listener.onGameOver(snake.size() - 3);
            return;
        }

        // Check self-collision
        for (int i = 1; i < snake.size(); i++) {
            Corner c = snake.get(i);
            if (c.x == head.x && c.y == head.y) {
                gameOver = true;
                stopGame();
                if (listener != null) listener.onGameOver(snake.size() - 3);
                return;
            }
        }

        // Check food collision
        if (head.x == foodX && head.y == foodY) {
            snake.add(new Corner(-1, -1));
            spawnFood();
        }
    }


    private void render() {
        // Clear canvas
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        // Draw grid (optional)
        gc.setStroke(Color.GRAY);
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        // Draw food
        Color fc = switch (foodColor) {
            case 0 -> Color.RED;
            case 1 -> Color.ORANGE;
            case 2 -> Color.YELLOW;
            case 3 -> Color.PURPLE;
            default -> Color.PINK;
        };
        gc.setFill(fc);
        gc.fillRect(foodX * cellSize, foodY * cellSize, cellSize - 1, cellSize - 1);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Corner c = snake.get(i);
            if (i == 0) gc.setFill(Color.DARKGREEN);
            else gc.setFill(Color.FORESTGREEN);
            gc.fillRect(c.x * cellSize, c.y * cellSize, cellSize - 1, cellSize - 1);
        }

        // Optional: draw game over text
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", width / 4.0, height / 2.0);
            gc.fillText("Score: " + (snake.size() - 3), width / 4.0, height / 2.0 + 30);
        }
    }


    private void spawnFood() {
        while (true) {
            foodX = rand.nextInt(cols);
            foodY = rand.nextInt(rows);

            boolean collision = false;
            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    collision = true;
                    break;
                }
            }
            if (!collision) break;
        }
        foodColor = rand.nextInt(5);
    }
}


*/