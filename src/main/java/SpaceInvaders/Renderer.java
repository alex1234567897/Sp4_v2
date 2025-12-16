package SpaceInvaders;

import javafx.scene.image.PixelWriter;

public class Renderer {
    private final PixelWriter pw;
    private final int w, h;
    private final Palette palette;

    public Renderer(PixelWriter pw, int w, int h, Palette palette) {
        this.pw = pw;
        this.w = w;
        this.h = h;
        this.palette = palette;
    }

    public int width()  { return w; }
    public int height() { return h; }

    public void pixel(int x, int y) {
        if (x < 0 || x >= w || y < 0 || y >= h) return;
        pw.setArgb(x, y, palette.colorForXY(x, y));
    }

    public void pixelColor(int x, int y, int argb) {
        if (x < 0 || x >= w || y < 0 || y >= h) return;
        pw.setArgb(x, y, argb);
    }

    public void sprite(Sprite s, int x, int y) {
        for (int sy = 0; sy < s.getHeight(); sy++) {
            int py = y + sy;
            if (py < 0 || py >= h) continue;

            for (int sx = 0; sx < s.getWidth(); sx++) {
                if (!s.isOn(sx, sy)) continue;
                int px = x + sx;
                if (px < 0 || px >= w) continue;

                pixel(px, py);
            }
        }
    }

    public void spriteCenteredX(Sprite s, int centerX, int y) {
        sprite(s, centerX - s.getWidth() / 2, y);
    }

    public Palette palette() { return palette; }
}