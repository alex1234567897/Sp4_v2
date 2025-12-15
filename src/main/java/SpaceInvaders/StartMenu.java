package SpaceInvaders;

import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;

import java.util.Set;

public class StartMenu implements Menu {

    private static final int MENU_OFFSET_X = 0;

    private final PixelWriter pw;
    private final Renderer renderer;
    private final BitmapFont font;
    private final int w, h;

    private boolean finished = false;

    private static final int ON  = Colors.ON;
    private static final int OFF = Colors.OFF;

    public StartMenu(PixelWriter pw, Renderer renderer,
                     BitmapFont font, int w, int h) {
        this.pw = pw;
        this.renderer = renderer;
        this.font = font;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(double dt, Set<KeyCode> keys) {
        if (keys.contains(KeyCode.SPACE)) {
            finished = true;
        }
    }

    @Override
    public void render() {
        clear();

        int topY = 12;
        int hiX = centerX("HI-SCORE");
        font.draw(pw, "HI-SCORE", hiX, topY, ink(topY), w, h);

        String his = "0000";
        int hisY = topY + 12;
        font.draw(pw, his, centerX(his), hisY, ink(hisY), w, h);

        int playY = 68;
        font.draw(pw, "PLAY", centerX("PLAY"), playY, ink(playY), w, h);

        int titleY = playY + 20;
        font.draw(pw, "SPACE  INVADERS", centerX("SPACE  INVADERS"), titleY, ink(titleY), w, h);

        int tableHeaderY = titleY + 28;
        font.draw(pw, "*SCORE ADVANCE TABLE*", centerX("*SCORE ADVANCE TABLE*"),
                tableHeaderY, ink(tableHeaderY), w, h);

        int baseY = tableHeaderY + 18;
        drawRow(Assets.UFO,    "?  MYSTERY",  baseY + 0);
        drawRow(Assets.SQUID,  "30 POINTS",   baseY + 16);
        drawRow(Assets.CRAB,   "20 POINTS",   baseY + 32);


        drawRowColored(Assets.OCTOPUS, "10 POINTS", baseY + 48, renderer.palette().greenInk(Colors.ON));

        int creditY = h - 10 - 7;
        String credit = "CREDIT  00";
        int creditX = w - textW(credit) - 16 + MENU_OFFSET_X;
        font.draw(pw, credit, creditX, creditY, ink(creditY), w, h);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    private void drawRow(Sprite s, String text, int y) {
        int sx = 78 + MENU_OFFSET_X;
        int eqX = sx + s.getWidth() + 2;
        int ty = y + 2;

        renderer.sprite(s, sx, y);
        font.draw(pw, "=", eqX, ty, ink(ty), w, h);
        font.draw(pw, " " + text, eqX + 6, ty, ink(ty), w, h);
    }

    private void drawRowColored(Sprite s, String text, int y, int color) {
        int sx = 78 + MENU_OFFSET_X;
        int eqX = sx + s.getWidth() + 2;
        int ty = y + 2;

        renderer.sprite(s, sx, y);
        font.draw(pw, "=", eqX, ty, color, w, h);
        font.draw(pw, " " + text, eqX + 6, ty, color, w, h);
    }

    private void clear() {
        for (int yy = 0; yy < h; yy++) {
            for (int xx = 0; xx < w; xx++) {
                pw.setArgb(xx, yy, OFF);
            }
        }
    }

    private int ink(int y) {
        return renderer.palette().inkAtY(y, Colors.ON);
    }

    private int textW(String s) {
        return font.measureWidth(s);
    }

    private int centerX(String s) {
        return (w - textW(s)) / 2 + MENU_OFFSET_X;
    }
}
