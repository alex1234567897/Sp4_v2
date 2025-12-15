package SpaceInvaders;

import javafx.scene.image.PixelWriter;

import java.util.Random;

public class Ufo {

    private int x, y;
    private int vx;

    private boolean active = false;
    private boolean hit = false;

    private double spawnTimer = 0.0;
    private double hitTimer = 0.0;
    private double showScoreTimer = 0.0;

    private static final double HIT_TIME   = 0.12;
    private static final double SCORE_TIME = 0.5;

    private String shownScore = "";
    private int shownScoreX, shownScoreY;

    public Ufo(int y) {
        this.y = y;
        scheduleNextSpawn(new Random());
    }

    public void update(double dt, int worldWidth, Random rng) {

        if (showScoreTimer > 0) {
            showScoreTimer -= dt;
            if (showScoreTimer <= 0) shownScore = "";
        }

        if (hit) {
            hitTimer -= dt;
            if (hitTimer <= 0) {
                hit = false;
                SoundManager.play(SoundManager.PLAYER_DIE);
                scheduleNextSpawn(rng);
            }
            return;
        }

        if (!active) {
            spawnTimer -= dt;
            if (spawnTimer <= 0) spawn(worldWidth, rng);
            return;
        }

        x += (int) Math.round(vx * dt);

        int w = Assets.UFO.getWidth();
        if ((vx > 0 && x > worldWidth + 2) || (vx < 0 && x < -w - 2)) {
            active = false;
            scheduleNextSpawn(rng);
        }
    }

    private void spawn(int worldWidth, Random rng) {
        int w = Assets.UFO.getWidth();

        if (rng.nextBoolean()) {
            x = -w - 2;
            vx = 60;
        } else {
            x = worldWidth + 2;
            vx = -60;
        }

        active = true;
    }

    private void scheduleNextSpawn(Random rng) {
        spawnTimer = 8.0 + rng.nextDouble() * 12.0;
    }

    public void render(Renderer r, BitmapFont font,
                       PixelWriter pw, int color,
                       int worldW, int worldH) {

        if (hit) {
            r.sprite(Assets.UFO_HIT, x, y);
        } else if (active) {
            r.sprite(Assets.UFO, x, y);
        }

        if (!shownScore.isEmpty()) {
            font.draw(pw, shownScore, shownScoreX, shownScoreY, color, worldW, worldH);
        }
    }

    public boolean hitTest(int worldX, int worldY) {
        if (!active || hit) return false;

        int lx = worldX - x;
        int ly = worldY - y;

        if (lx < 0 || lx >= Assets.UFO.getWidth()) return false;
        if (ly < 0 || ly >= Assets.UFO.getHeight()) return false;

        return Assets.UFO.isOn(lx, ly);
    }

    public int onShot(Random rng) {
        if (!active || hit) return 0;

        int[] pts = {50, 100, 150, 300};
        int p = pts[rng.nextInt(pts.length)];

        active = false;
        hit = true;
        hitTimer = HIT_TIME;
        vx = 0;

        shownScore = String.valueOf(p);
        shownScoreX = x + 2;
        shownScoreY = y + 2;
        showScoreTimer = SCORE_TIME;

        return p;
    }
}
