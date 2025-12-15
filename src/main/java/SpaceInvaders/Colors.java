package SpaceInvaders;

public final class Colors {
    private Colors() {}

    public static final int OFF = argb(255, 0, 0, 0);
    public static final int ON  = argb(255, 220, 255, 220);

    private static int argb(int a, int r, int g, int b) {
        return ((a & 255) << 24)
                | ((r & 255) << 16)
                | ((g & 255) << 8)
                |  (b & 255);
    }
}