package SpaceInvaders;

public class Player {
    private double x, y;
    private final Sprite sprite = Assets.PLAYER;

    private boolean dying = false;
    private double dyingTimer = 0.0;
    private static final double DYING_TIME = 0.45; // death visuel tid
    private final Animation deathAnim = Assets.playerDeathAnim();

    private double speed = 80;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(double dt, int dir, int worldWidth) {
        if (dying) {
            dyingTimer -= dt;
            deathAnim.update(dt);
            return;
        }

        x += dir * speed * dt;
        double maxX = worldWidth - sprite.getWidth();
        if (x < 0) x = 0;
        if (x > maxX) x = maxX;
    }

    public void render(Renderer r) {
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);

        if (dying) {
            r.sprite(deathAnim.getFrame(), ix, iy);
        } else {
            r.sprite(sprite, ix, iy);
        }
    }

    public int getGunX() {
        return (int)Math.round(x) + sprite.getWidth() / 2;
    }

    public int getGunY() {
        return (int)Math.round(y);
    }

    public boolean hitTest(int worldX, int worldY) {
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);

        int lx = worldX - ix;
        int ly = worldY - iy;

        if (lx < 0 || lx >= sprite.getWidth() || ly < 0 || ly >= sprite.getHeight()) return false;
        return sprite.isOn(lx, ly);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getW() {
        return sprite.getWidth();
    }

    public int getH() {
        return sprite.getHeight();
    }

    public int getCenterX() {
        return (int)Math.round(x) + sprite.getWidth() / 2;
    }

    public boolean isDying() { return dying; }

    public void die() {
        dying = true;
        dyingTimer = DYING_TIME;
        deathAnim.reset();
    }

    public void updateDeath(double dt) {
        if (!dying) return;
        deathAnim.update(dt);
    }

    public void revive() {
        dying = false;
    }
}
