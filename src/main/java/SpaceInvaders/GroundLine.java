package SpaceInvaders;

public class GroundLine {
    private final int y;
    private final boolean[] on;

    public GroundLine(int worldWidth, int y) {
        this.y = y;
        this.on = new boolean[worldWidth];
        for (int i = 0; i < on.length; i++) on[i] = true;
    }

    public int getY() { return y; }

    public void punch(int x, int radius) {
        for (int i = x - radius; i <= x + radius; i++) {
            if (i >= 0 && i < on.length) on[i] = false;
        }
    }

    public boolean isOnAt(int x) {
        return x >= 0 && x < on.length && on[x];
    }

    public void render(Renderer r) {
        for (int x = 0; x < on.length; x++) {
            if (on[x]) r.pixel(x, y);
        }
    }
}