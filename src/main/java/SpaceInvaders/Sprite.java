package SpaceInvaders;

public class Sprite {

    private final boolean[][] pixels;
    private final int width;
    private final int height;

    private Sprite(boolean[][] pixels) {
        this.pixels = pixels;
        this.height = pixels.length;
        this.width = pixels[0].length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isOn(int x, int y) {
        return pixels[y][x];
    }

    public static Sprite fromStrings(String... rows) {
        int h = rows.length;

        String[] cleaned = new String[h];
        for (int i = 0; i < h; i++) {
            cleaned[i] = rtrim(rows[i]);
        }

        int w = cleaned[0].length();
        boolean[][] data = new boolean[h][w];

        for (int y = 0; y < h; y++) {
            String row = cleaned[y];
            if (row.length() != w) {

                throw new IllegalArgumentException(
                        "Invalid row length at row " + y + ": " + row.length() +
                                " (expected " + w + ")\nRow='" + row + "'\nCodes=" + codes(row)
                );
            }
            for (int x = 0; x < w; x++) {
                data[y][x] = (row.charAt(x) == '#');
            }
        }

        return new Sprite(data);
    }

    private static String rtrim(String s) {
        int end = s.length();
        while (end > 0) {
            char c = s.charAt(end - 1);
            if (c == ' ' || c == '\t') end--;
            else break;
        }
        return s.substring(0, end);
    }

    private static String codes(String s) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < s.length(); i++) {
            if (i > 0) sb.append(",");
            sb.append((int) s.charAt(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
