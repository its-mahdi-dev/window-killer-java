package gradle.controller;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Constants {
        public static final Dimension GAME_FRAME_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        public static final Dimension PANEL_SIZE = new Dimension((int) (GAME_FRAME_DIMENSION.getWidth() / 2),
                        (int) (GAME_FRAME_DIMENSION.getHeight() / 2));
        public static final int FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
                        .getDisplayMode().getRefreshRate();
        public static final double FRAME_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / FPS;
        public static final int UPS = 120;
        public static final double MODEL_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / UPS;
        public static final double SPEED = 3 * 60D / UPS;
        public static final double ACCELERATION = 24 * 60D / UPS;

        public static final double CHANGE_FRAME_SPEED = 3 * 60D / UPS;

        // Entities
        public static final int EPSILON_DIAMETER = 30;
        public static final double MOVE_SPEED = 12 * 60D / UPS;
        public static final int DIAGONAL_SPEED = 2;
        public static final int SHOT_DIAMETER = 6;
        public static final double SHOT_SPEED = 25 * 60D / UPS;

        // ENEMIES
        public static final int ENEMY_SQUARE_DIAMETER = 50;
        public static final int ENEMY_TRIANGLE_DIAMETER = 40;
        public static final double ENEMY_SPEED = 1 * 60D / UPS;

        public static final double ENEMY_STROKE = 3.0;
}
