package SpaceInvaders;

import javafx.scene.media.AudioClip;

public class SoundManager {

    public static AudioClip PLAYER_SHOOT;
    public static AudioClip PLAYER_DIE;
    public static AudioClip INVADER_SHOOT;

    static {
        try {
            PLAYER_SHOOT = load("/sounds/shoot.wav");
            PLAYER_DIE   = load("/sounds/explosion.wav");
            INVADER_SHOOT = load("/sounds/invader_shoot.wav");
        } catch (Exception e) {
            System.err.println("Kunne ikke indlæse lydfiler: " + e);
        }
    }

    private static AudioClip load(String path) {
        var res = SoundManager.class.getResource(path);
        if (res == null) {
            System.err.println("Filen findes ikke: " + path);
            return null;
        }
        return new AudioClip(res.toExternalForm());
    }

    public static void play(AudioClip clip) {
        if (clip != null) clip.play();
    }
}