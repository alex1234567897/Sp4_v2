package SpaceInvaders;

public final class Layout {
    private Layout() {}

    // Farve-zoner
    public static final int RED_BAR_TOP_FROM_TOP = 32;
    public static final int RED_BAR_HEIGHT = 32;

    public static final int TOP_GREEN_HEIGHT = 56;
    public static final int TOP_GREEN_BOTTOM_MARGIN = 16;

    public static final int BOTTOM_GREEN_HEIGHT = 16;
    public static final int BOTTOM_GREEN_LEFT_MARGIN = 25;
    public static final int BOTTOM_GREEN_RIGHT_MARGIN = 88;

    //Invader start position
    public static final int INVADERS_START_Y = 63;


    // SCORE HUD
    public static final int SCORE_LABEL_X = 8;
    public static final int SCORE_NUMBER_X = 24;

    public static final int SCORE_LABEL_Y = 12;
    public static final int SCORE_NUMBER_Y = SCORE_LABEL_Y + 7 + 8;

    // Ground line + lives area
    public static final int GROUND_FROM_BOTTOM = 17;
    public static final int LIVES_NUMBER_X_FROM_LEFT = 9;
    public static final int LIVES_NUMBER_Y_OFFSET_UNDER_LINE = 1;

    public static final int GAP_NUMBER_TO_LIFE_ICON = 9;
    public static final int GAP_BETWEEN_LIFE_ICONS = 1;

    // World placement
    public static final int PLAYER_BOTTOM_FROM_BOTTOM = 32;
    public static final int BARRIER_BOTTOM_FROM_BOTTOM = 48;

    public static final int BARRIER_FIRST_X_FROM_LEFT = 32;
    public static final int BARRIER_GAP_X = 23;

    public static int ufoY(int ufoHeight) {
        return RED_BAR_TOP_FROM_TOP + (RED_BAR_HEIGHT - ufoHeight) / 2;
    }
}
