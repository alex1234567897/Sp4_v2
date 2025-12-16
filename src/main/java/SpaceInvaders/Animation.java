package SpaceInvaders;

public final class Animation {
    private final Sprite[] frames;
    private final double frameTime;
    private int index = 0;
    private double acc = 0;

    public Animation(double frameTime, Sprite... frames) {
        if (frames.length == 0) throw new IllegalArgumentException("No frames");
        this.frames = frames;
        this.frameTime = frameTime;
    }

    public void update(double dt) {
        acc += dt;
        while (acc >= frameTime) {
            acc -= frameTime;
            index = (index + 1) % frames.length;
        }
    }

    public Sprite getFrame() {
        return frames[index];
    }

    public void reset() {
        index = 0;
        acc = 0;
    }
}