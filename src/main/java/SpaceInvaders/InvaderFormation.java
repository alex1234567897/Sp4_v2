package SpaceInvaders;

import java.util.*;

public class InvaderFormation {

    private final List<Invader> invaders = new ArrayList<>();

    private int dir = 1;
    private double stepTimer = 0;
    private double stepInterval = 0.55;
    private int stepX = 2;
    private int stepDown = 8;

    private int initialAlive = 1;

    private static final double START_INTERVAL = 0.60;
    private static final double MIN_INTERVAL   = 0.08;

    private final int worldWidth;

    private double shootTimer = 0;
    private double shootInterval = 0.8;
    private final Random rng = new Random();


    public InvaderFormation(int worldWidth) {
        this.worldWidth = worldWidth;
    }

    public void buildClassicGrid(int startX, int startY, int cols, int rows, int gapX, int gapY) {
        invaders.clear();

        for (int r = 0; r < rows; r++) {
            InvaderType type =
                (r == 0) ? InvaderType.SQUID :
                (r <= 2) ? InvaderType.CRAB :
                InvaderType.OCTOPUS;

            for (int c = 0; c < cols; c++) {
                int x = startX + c * gapX;
                int y = startY + r * gapY;
                invaders.add(new Invader(x, y, type));
            }
        }

        initialAlive = countAlive();
        if (initialAlive <= 0) initialAlive = 1;
    }

    public void update(double dt) {

        for (Invader i : invaders) i.update(dt);

        int aliveCount = 0;
        for (Invader i : invaders) if (i.isAlive()) aliveCount++;

        if (aliveCount == 0) return;

        double t = 1.0 - (aliveCount / (double) initialAlive);
        t = Math.max(0.0, Math.min(1.0, t));
        t = t * t;

        stepInterval = lerp(0.60, 0.08, t);

        stepTimer += dt;
        if (stepTimer < stepInterval) return;
        stepTimer -= stepInterval; // ✅ stabilt

        int dx = dir * stepX;

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (Invader i : invaders) {
            if (!i.isAlive()) continue;
            minX = Math.min(minX, i.getX());
            maxX = Math.max(maxX, i.getX() + i.getW());
        }

        if (minX == Integer.MAX_VALUE) return;

        boolean hitsRight = (maxX + dx) >= (worldWidth - 1);
        boolean hitsLeft  = (minX + dx) <= 0;

        if (hitsRight || hitsLeft) {
            for (Invader i : invaders) i.moveBy(0, stepDown);
            dir *= -1;
        } else {
            for (Invader i : invaders) i.moveBy(dx, 0);
        }

        shootTimer += dt;
        if (shootTimer >= shootInterval) {
            shootTimer = 0;

            List<Invader> shooters = getBottomInvaders();
            if (!shooters.isEmpty()) {
                Invader shooter = shooters.get(rng.nextInt(shooters.size()));

                int bx = shooter.getX() + shooter.getW() / 2;
                int by = shooter.getY() + shooter.getH();
            }
        }
    }

    public void render(Renderer r) {
        for (Invader i : invaders) {
            i.render(r);
        }
    }

    public List<Invader> getBottomInvaders() {
        Map<Integer, Invader> bottoms = new HashMap<>();

        for (Invader i : invaders) {
            if (!i.isAlive()) continue;

            int colX = i.getX();
            Invader existing = bottoms.get(colX);

            if (existing == null || i.getY() > existing.getY()) {
                bottoms.put(colX, i);
            }
        }
        return new ArrayList<>(bottoms.values());
    }

    public Invader requestRandomBottomShooter() {
        Map<Integer, Invader> bottoms = new HashMap<>();

        for (Invader i : invaders) {
            if (!i.isAlive()) continue;

            int colX = i.getX();
            Invader existing = bottoms.get(colX);

            if (existing == null || i.getY() > existing.getY()) {
                bottoms.put(colX, i);
            }
        }

        if (bottoms.isEmpty()) return null;

        List<Invader> list = new ArrayList<>(bottoms.values());
        return list.get(rng.nextInt(list.size()));
    }

    public Invader hitTest(int worldX, int worldY) {
        for (Invader i : invaders) {
            if (!i.isAlive() || i.isDying()) continue;

            if (i.hitTest(worldX, worldY)) return i;
        }
        return null;
    }

    private int countAlive() {
        int c = 0;
        for (Invader inv : invaders) {
            if (inv.isAlive()) c++;
        }
        return c;
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}