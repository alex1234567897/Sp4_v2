package SpaceInvaders;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.*;

public class GameLoop {

    private GameState state = GameState.MENU;
    private Menu currentMenu;

    private final int w, h, scale;

    private final Palette palette;
    private Renderer renderer;

    private double invulnTimer = 0.0;
    private static final double INVULN_TIME = 1.2;

    private GroundLine ground;
    private int lives = 3;
    private BitmapFont font = Assets.font5x7();

    private int score = 0;

    private final WritableImage framebuffer;
    private final PixelWriter pixelW;
    private final ImageView view;
    private final StackPane root;

    private final Set<KeyCode> keys = new HashSet<>();

    private final Player player;
    private Barrier[] barriers;
    private InvaderFormation formation;
    private Ufo ufo;

    private double freezeTimer = 0.0;
    private static final double FREEZE_TIME = 1.0;

    private Bullet playerBullet = null;
    private double shootCooldown = 0.0;

    private static final int OFF = Colors.OFF;
    private static final int ON  = Colors.ON;

    private double invaderShootTimer = 0;
    private double invaderShootInterval = 0.8;
    private final Random rng = new Random();
    private final List<Bullet> invaderBullets = new ArrayList<>();

    private AnimationTimer loop;

    public GameLoop(int w, int h, int scale) {
        this.w = w;
        this.h = h;
        this.scale = scale;

        framebuffer = new WritableImage(w, h);
        pixelW = framebuffer.getPixelWriter();

        palette = new Palette(w, h);
        renderer = new Renderer(pixelW, w, h, palette);

        currentMenu = new StartMenu(pixelW, renderer, font, w, h);

        ground = new GroundLine(w, h - Layout.GROUND_FROM_BOTTOM);

        view = new ImageView(framebuffer);
        view.setSmooth(false);
        view.setFitWidth(w * scale);
        view.setFitHeight(h * scale);

        root = new StackPane(view);

        root.setFocusTraversable(true);
        view.setFocusTraversable(true);

        int playerX = (w - Assets.PLAYER.getWidth()) / 2;
        int playerY = h - Layout.PLAYER_BOTTOM_FROM_BOTTOM - Assets.PLAYER.getHeight();
        player = new Player(playerX, playerY);


        formation = new InvaderFormation(w);
        formation.buildClassicGrid(20, Layout.INVADERS_START_Y, 11, 5, 14, 14);

        int ufoY = Layout.ufoY(Assets.UFO.getHeight());
        ufo = new Ufo(ufoY);

        Sprite bunker = Assets.BARRIER;

        int barrierY = h - Layout.BARRIER_BOTTOM_FROM_BOTTOM - Assets.BARRIER.getHeight();

        int bx0 = Layout.BARRIER_FIRST_X_FROM_LEFT;
        int step = Assets.BARRIER.getWidth() + Layout.BARRIER_GAP_X;

        barriers = new Barrier[] {
                new Barrier(bx0 + 0 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 1 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 2 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 3 * step, barrierY, Assets.BARRIER)
        };


    }

    public Parent getRoot() {
        return root;
    }

    public void requestFocus() {
        root.requestFocus();
    }

    public void attachInput(Scene scene) {
        scene.setOnKeyPressed(e -> keys.add(e.getCode()));
        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));
    }

    public void start() {
        loop = new AnimationTimer() {
            long lastNs = 0;

            @Override
            public void handle(long now) {
                if (lastNs == 0) {
                    lastNs = now;
                    return;
                }
                double dt = (now - lastNs) / 1_000_000_000.0;
                lastNs = now;

                update(dt);
                render();
            }
        };
        loop.start();
    }

    private void update(double dt) {

        if (freezeTimer > 0) {
            freezeTimer -= dt;

            player.updateDeath(dt);

            if (freezeTimer <= 0) {
                if (lives > 0) {
                    respawnPlayer();
                } else {
                    // game over state senere
                }
            }
            return;
        }

        if (state == GameState.MENU) {
            currentMenu.update(dt, keys);

            if (currentMenu.isFinished()) {
                startNewGame();
                state = GameState.PLAYING;
            }
            return;
        }

        ufo.update(dt, w, rng);

        if (invulnTimer > 0) invulnTimer -= dt;

        int dir = 0;
        if (keys.contains(KeyCode.LEFT) || keys.contains(KeyCode.A)) dir -= 1;
        if (keys.contains(KeyCode.RIGHT) || keys.contains(KeyCode.D)) dir += 1;
        player.update(dt, dir, w);

        formation.update(dt);

        if (shootCooldown > 0) shootCooldown -= dt;

        boolean wantsShoot = keys.contains(KeyCode.SPACE);
        if (!player.isDying() && wantsShoot && shootCooldown <= 0
                && (playerBullet == null || !playerBullet.isAlive())) {

            int bx = player.getGunX();
            int by = player.getGunY() - 6;
            playerBullet = new Bullet(bx, by, -180, Assets.PLAYER_BULLET);
            shootCooldown = 0.25;
        }

        if (playerBullet != null && playerBullet.isAlive()) {
            playerBullet.update(dt, h);

            int x = playerBullet.getX();
            int y0 = playerBullet.getPrevY();
            int y1 = playerBullet.getY();

            int step = (y1 >= y0) ? 1 : -1;
            for (int y = y0; y != y1 + step; y += step) {

                if (checkBulletHitsBarrier(x, y)) {
                    playerBullet.impact();
                    break;
                }

                Invader hit = formation.hitTest(x, y);
                if (hit != null) {
                    hit.die();
                    score += hit.getPoints();
                    playerBullet.impact();
                    break;
                }

                if (ufo != null && ufo.hitTest(x, y)) {
                    int gained = ufo.onShot(rng);
                    if (gained > 0) score += gained;

                    playerBullet.kill();
                    break;
                }
            }
        }

        invaderShootTimer += dt;
        if (invaderShootTimer >= invaderShootInterval) {
            invaderShootTimer = 0;

            if (invaderBullets.size() < 3) {
                Invader shooter = formation.requestRandomBottomShooter();
                if (shooter != null) {

                    InvaderShotType type = pickShotType();

                    Animation anim = switch (type) {
                        case ROLLING  -> Assets.invaderRollingBullet();
                        case PLUNGER  -> Assets.invaderPlungerBullet();
                        case SQUIGGLY -> Assets.invaderSquigglyBullet();
                    };

                    int bx = shooter.getX() + shooter.getW() / 2;
                    int by = shooter.getY() + shooter.getH();
                    invaderBullets.add(new Bullet(bx, by, 120, anim, type));
                }
            }
        }

        Iterator<Bullet> it = invaderBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();

            b.update(dt, h);

            if (!b.isAlive()) {
                it.remove();
                continue;
            }

            int bx = b.getX();
            int y0 = b.getPrevY();
            int y1 = b.getY();

            int step = (y1 >= y0) ? 1 : -1;
            boolean hit = false;

            for (int y = y0; y != y1 + step; y += step) {

                if (checkBulletHitsBarrier(bx, y)) {
                    hit = true;
                    break;
                }

                if (ground != null && y == ground.getY() && ground.isOnAt(bx)) {
                    ground.punch(bx, 2);
                    hit = true;
                    break;
                }

                if (invulnTimer <= 0 && player.hitTest(bx, y) && !player.isDying()) {
                    lives--;

                    player.die();
                    freezeTimer = FREEZE_TIME;

                    if (playerBullet != null) playerBullet.kill();
                    invaderBullets.clear();

                    hit = true;
                    break;
                }
            }

            if (hit) b.impact();
        }
    }


    private void render() {
        clear(OFF);

        if (state == GameState.MENU) {
            currentMenu.render();
            return;
        }

        font.draw(pixelW, "SCORE<1>", Layout.SCORE_LABEL_X, Layout.SCORE_LABEL_Y, ON, w, h);
        font.draw(pixelW, String.format("%04d", score), Layout.SCORE_NUMBER_X, Layout.SCORE_NUMBER_Y, ON, w, h);

        for (Barrier b : barriers) b.render(renderer);

        formation.render(renderer);
        ufo.render(renderer, font, pixelW, ON, w, h);

        player.render(renderer);

        if (playerBullet != null && playerBullet.isAlive()) playerBullet.render(renderer);
        for (Bullet b : invaderBullets) b.render(renderer);

        if (ground != null) ground.render(renderer);
        drawLives();
    }

    private void clear(int color) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixelW.setArgb(x, y, color);
            }
        }
    }

    private static int argb(int a, int r, int g, int b) {
        return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    private boolean checkBulletHitsBarrier(int x, int y) {
        for (Barrier b : barriers) {
            if (b.isSolidWorld(x, y)) {
                b.punchHoleWorld(x, y, 3);
                return true;
            }
        }
        return false;
    }

    private void drawLives() {
        int lineY = h - Layout.GROUND_FROM_BOTTOM;

        int numberX = Layout.LIVES_NUMBER_X_FROM_LEFT;
        int numberY = lineY + Layout.LIVES_NUMBER_Y_OFFSET_UNDER_LINE;
        font.draw(pixelW, String.valueOf(lives), numberX, numberY, ON, w, h);

        int iconsX = numberX + Layout.GAP_NUMBER_TO_LIFE_ICON + 5;
        int iconY  = numberY;

        int icons = Math.max(0, lives - 1);
        int step = Assets.PLAYER.getWidth() + Layout.GAP_BETWEEN_LIFE_ICONS;

        for (int i = 0; i < icons; i++) {
            drawSprite(Assets.PLAYER, iconsX + i * step, iconY, ON);
        }
    }

    private void drawSprite(Sprite s, int x, int y, int color) {
        for (int sy = 0; sy < s.getHeight(); sy++) {
            int py = y + sy;
            if (py < 0 || py >= h) continue;

            for (int sx = 0; sx < s.getWidth(); sx++) {
                int px = x + sx;
                if (px < 0 || px >= w) continue;

                if (s.isOn(sx, sy)) pixelW.setArgb(px, py, color);
            }
        }
    }

    private InvaderShotType pickShotType() {
        if (!hasRollingShotActive()) return InvaderShotType.ROLLING;

        return rng.nextBoolean()
                ? InvaderShotType.PLUNGER
                : InvaderShotType.SQUIGGLY;
    }

    private boolean hasRollingShotActive() {
        for (Bullet b : invaderBullets) {
            if (b.getType() == InvaderShotType.ROLLING) return true;
        }
        return false;
    }

    private void respawnPlayer() {
        int playerX = (w - Assets.PLAYER.getWidth()) / 2;
        int playerY = h - Layout.PLAYER_BOTTOM_FROM_BOTTOM - Assets.PLAYER.getHeight();
        player.setX(playerX);
        player.setY(playerY);
        player.revive();

        invulnTimer = INVULN_TIME;
    }

    private void startNewGame() {
        score = 0;
        lives = 3;

        invulnTimer = 0;
        freezeTimer = 0;
        shootCooldown = 0;

        playerBullet = null;
        invaderBullets.clear();

        int playerX = (w - Assets.PLAYER.getWidth()) / 2;
        int playerY = h - Layout.PLAYER_BOTTOM_FROM_BOTTOM - Assets.PLAYER.getHeight();
        player.setX(playerX);
        player.setY(playerY);
        player.revive();

        formation.buildClassicGrid(20, Layout.INVADERS_START_Y, 11, 5, 14, 14);

        int barrierY = h - Layout.BARRIER_BOTTOM_FROM_BOTTOM - Assets.BARRIER.getHeight();
        int bx0 = Layout.BARRIER_FIRST_X_FROM_LEFT;
        int step = Assets.BARRIER.getWidth() + Layout.BARRIER_GAP_X;

        barriers = new Barrier[] {
                new Barrier(bx0 + 0 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 1 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 2 * step, barrierY, Assets.BARRIER),
                new Barrier(bx0 + 3 * step, barrierY, Assets.BARRIER)
        };

        ground = new GroundLine(w, h - Layout.GROUND_FROM_BOTTOM);

        int ufoY = Layout.ufoY(Assets.UFO.getHeight());
        ufo = new Ufo(ufoY);
    }
}