package SpaceInvaders;

import javafx.scene.image.PixelWriter;

import java.util.HashMap;
import java.util.Map;

public class BitmapFont {
    private final Map<Character, Sprite> glyphs = new HashMap<>();
    private final int spacing;

    public BitmapFont(int spacing) {
        this.spacing = spacing;
    }

    public void put(char c, Sprite s) {
        glyphs.put(c, s);
    }

    public void draw(PixelWriter pw, String text, int x, int y, int color, int worldW, int worldH) {
        int cx = x;

        for (char c : text.toCharArray()) {
            if (c == ' ') { cx += 3 + spacing; continue; }

            Sprite s = glyphs.get(c);
            if (s == null) { cx += 3 + spacing; continue; }

            for (int sy = 0; sy < s.getHeight(); sy++) {
                int py = y + sy;
                if (py < 0 || py >= worldH) continue;

                for (int sx = 0; sx < s.getWidth(); sx++) {
                    int px = cx + sx;
                    if (px < 0 || px >= worldW) continue;

                    if (s.isOn(sx, sy)) {
                        pw.setArgb(px, py, color);
                    }
                }
            }

            cx += s.getWidth() + spacing;
        }
    }

    public int measureWidth(String text) {
        int width = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                width += 3 + spacing;
                continue;
            }
            Sprite s = glyphs.get(c);
            if (s != null) width += s.getWidth() + spacing;
            else width += 3 + spacing;
        }
        if (width > 0) width -= spacing;
        return width;
    }
}