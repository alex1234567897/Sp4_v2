package SpaceInvaders;

public class Invader {
    private int x, y;
    private final Animation anim;
    private boolean alive = true;
    private boolean dying = false;
    private double dyingTimer = 0.0;
    private static final double DYING_TIME = 0.12;
    private final InvaderType type;

    public Invader(int x, int y, InvaderType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.anim = type.createAnim();
    }

    public void update(double dt) {
        if (!alive) return;

        if (dying) {
            dyingTimer -= dt;
            if (dyingTimer <= 0) {
                alive = false;
                dying = false;
            }
            return;
        }

        anim.update(dt);
    }

    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void render(Renderer r) {
        if (!alive) return;

        if (dying) {
            r.sprite(Assets.INVADER_DEATH, x, y);
        } else {
            r.sprite(anim.getFrame(), x, y);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDying() {
        return dying;
    }

    public void kill() {
        alive = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return anim.getFrame().getWidth();
    }

    public int getH() {
        return anim.getFrame().getHeight();
    }

    public void die() {
        if (!alive || dying) return;
        dying = true;
        dyingTimer = DYING_TIME;
        SoundManager.play(SoundManager.PLAYER_DIE);
    }

    public int getPoints() {
        return type.points();
    }

    public boolean hitTest(int worldX, int worldY) {
        int lx = worldX - x;
        int ly = worldY - y;
        if (lx < 0 || lx >= anim.getFrame().getWidth() || ly < 0 || ly >= anim.getFrame().getHeight()) return false;
        return anim.getFrame().isOn(lx, ly);
    }
}
