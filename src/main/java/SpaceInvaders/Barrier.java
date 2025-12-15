package SpaceInvaders;

public class Barrier {
    private final int x, y;
    private final int w, h;
    private final boolean[][] pixels;

    public Barrier(int x, int y, Sprite shape) {
        this.x = x;
        this.y = y;
        this.w = shape.getWidth();
        this.h = shape.getHeight();

        this.pixels = new boolean[h][w];
        for (int yy = 0; yy < h; yy++) {
            for (int xx = 0; xx < w; xx++) {
                pixels[yy][xx] = shape.isOn(xx, yy);
            }
        }
    }

    public void render(Renderer r) {
        for (int yy = 0; yy < h; yy++) {
            int py = y + yy;
            for (int xx = 0; xx < w; xx++) {
                if (!pixels[yy][xx]) continue;
                r.pixel(x + xx, py);
            }
        }
    }

    public void punchHoleWorld(int hitX, int hitY, int radius) {
        int cx = hitX - x;
        int cy = hitY - y;
        punchHoleLocal(cx, cy, radius);
    }

    public void punchHoleLocal(int cx, int cy, int radius) {
        int r2 = radius * radius;
        for (int yy = cy - radius; yy <= cy + radius; yy++) {
            if (yy < 0 || yy >= h) continue;
            for (int xx = cx - radius; xx <= cx + radius; xx++) {
                if (xx < 0 || xx >= w) continue;
                int dx = xx - cx, dy = yy - cy;
                if (dx * dx + dy * dy <= r2) {
                    pixels[yy][xx] = false;
                }
            }
        }
    }

    public boolean isSolidWorld(int worldX, int worldY) {
        int lx = worldX - x;
        int ly = worldY - y;
        if (lx < 0 || lx >= w || ly < 0 || ly >= h) return false;
        return pixels[ly][lx];
    }
}
