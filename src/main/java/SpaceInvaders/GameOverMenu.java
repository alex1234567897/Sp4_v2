package SpaceInvaders;

import com.example.icesp4.MainMenu;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;

import java.util.Set;
import java.util.function.IntConsumer;

public class GameOverMenu implements Menu {

    private final PixelWriter pw;
    private final Renderer renderer;
    private final BitmapFont font;
    private final int w, h;

    private final int finalScore;
    private final Runnable onRetry;
    private final IntConsumer onExitToOuterMenu;

    private boolean finishedRetry = false;
    private boolean finishedExit = false;

    // typewriter
    private static final String MSG = "GAME OVER";
    private int shownChars = 0;
    private double typeTimer = 0.0;
    private static final double TYPE_INTERVAL = 0.12;

    public GameOverMenu(PixelWriter pw, Renderer renderer, BitmapFont font,
                        int w, int h,
                        int finalScore,
                        Runnable onRetry,
                        IntConsumer onExitToOuterMenu) {
        this.pw = pw;
        this.renderer = renderer;
        this.font = font;
        this.w = w;
        this.h = h;
        this.finalScore = finalScore;
        this.onRetry = onRetry;
        this.onExitToOuterMenu = onExitToOuterMenu;
    }

    @Override
    public void update(double dt, Set<KeyCode> keys) {
        if (shownChars < MSG.length()) {
            typeTimer += dt;
            while (typeTimer >= TYPE_INTERVAL && shownChars < MSG.length()) {
                typeTimer -= TYPE_INTERVAL;
                shownChars++;
            }
        }

        if (shownChars >= MSG.length()) {
            if (keys.contains(KeyCode.SPACE)) {
                finishedRetry = true;
            }
            if (keys.contains(KeyCode.ESCAPE)) {
                finishedExit = true;
                MainMenu.showHighscoreMenu();
            }
        }
    }

    @Override
    public void render() {
        clear(Colors.OFF);

        int y = 110;
        String partial = MSG.substring(0, shownChars);
        font.draw(pw, partial, centerX(partial), y, ink(y), w, h);

        if (shownChars >= MSG.length()) {
            int y2 = y + 22;
            font.draw(pw, "SPACE FOR RETRY", centerX("SPACE FOR RETRY"), y2, ink(y2), w, h);

            int y3 = y2 + 14;
            font.draw(pw, "ESC FOR EXIT", centerX("ESC FOR EXIT"), y3, ink(y3), w, h);
        }
    }

    @Override
    public boolean isFinished() {
        return finishedRetry || finishedExit;
    }

    public boolean wantsRetry() { return finishedRetry; }
    public boolean wantsExit()  { return finishedExit; }

    public void runAction() {
        if (finishedRetry) onRetry.run();
        if (finishedExit)  onExitToOuterMenu.accept(finalScore);
    }

    private void clear(int color) {
        for (int yy = 0; yy < h; yy++) {
            for (int xx = 0; xx < w; xx++) pw.setArgb(xx, yy, color);
        }
    }

    private int ink(int y) { return renderer.palette().inkAtY(y, Colors.ON); }

    private int centerX(String s) {
        return (w - s.length() * 6) / 2;
    }
}
