package SpaceInvaders;

public class Bullet {
    private double x, y;
    private double prevY;

    private final double vy;
    private final Animation anim;
    private final Sprite sprite;

    private final InvaderShotType type;

    private boolean alive = true;

    private boolean impacting = false;
    private double impactTimer = 0.0;
    private static final double IMPACT_TIME = 0.08;

    public Bullet(int x, int y, int vy, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.prevY = y;
        this.vy = vy;
        this.sprite = sprite;
        this.anim = null;
        this.type = null;
    }

    public Bullet(int x, int y, int vy, Animation anim, InvaderShotType type) {
        this.x = x;
        this.y = y;
        this.prevY = y;
        this.vy = vy;
        this.anim = anim;
        this.sprite = null;
        this.type = type;
    }

    public void update(double dt, int worldH) {
        update(dt, worldH, null);
    }

    public void update(double dt, int worldH, Player player) {
        if (!alive) return;

        if (impacting) {
            impactTimer -= dt;
            if (impactTimer <= 0) alive = false;
            return;
        }

        prevY = y;
        y += vy * dt;

        if (anim != null) {
            anim.update(dt);
        }

        if (y < 0 || y >= worldH) alive = false;
    }

    public void render(Renderer r) {
        if (!alive) return;

        if (impacting) {
            r.spriteCenteredX(Assets.IMPACT, getX(), getY());
            return;
        }

        if (sprite != null) {
            r.spriteCenteredX(sprite, getX(), getY());
        } else if (anim != null) {
            r.spriteCenteredX(anim.getFrame(), getX(), getY());
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public int getX() {
        return (int)Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public int getPrevY() {
        return (int)Math.round(prevY);
    }

    public void impact() {
        impacting = true;
        impactTimer = IMPACT_TIME;
    }

    public InvaderShotType getType() {
        return type;
    }
}