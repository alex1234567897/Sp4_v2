package SpaceInvaders;

public enum InvaderType {
    SQUID,
    CRAB,
    OCTOPUS;

    public Animation createAnim() {
        return switch (this) {
            case SQUID    -> Assets.squidAnim();
            case CRAB     -> Assets.crabAnim();
            case OCTOPUS  -> Assets.octopusAnim();
        };
    }

    public int points() {
        return switch (this) {
            case SQUID -> 30;
            case CRAB -> 20;
            case OCTOPUS -> 10;
        };
    }
}