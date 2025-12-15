package SpaceInvaders;

import javafx.scene.input.KeyCode;

import java.util.Set;

public interface Menu {
    void update(double dt, Set<KeyCode> keys);
    void render();
    boolean isFinished();
}
