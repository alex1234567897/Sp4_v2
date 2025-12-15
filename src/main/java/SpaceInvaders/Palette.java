package SpaceInvaders;

public class Palette {
    public static final int WHITE = 0xFFFFFFFF;
    public static final int GREEN = 0xFF00FF00;
    public static final int RED   = 0xFFFF4040;

    private final int w, h;

    public Palette(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public int colorForXY(int x, int y) {

        if (y >= 32 && y < 64) {
            return RED;
        }

        if (y >= h - 16 - 56 && y < h - 16) {
            return GREEN;
        }

        if (y >= h - 16) {
            if (x >= 25 && x < w - 88) {
                return GREEN;
            }
        }

        return WHITE;
    }

    public int inkAtY(int y, int fallbackWhite) {
        return fallbackWhite;
    }

    public int greenInk(int fallback) {
        return 0xFF00FF00;
    }
}