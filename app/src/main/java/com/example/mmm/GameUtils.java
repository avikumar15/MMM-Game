package com.example.mmm;

import java.util.Random;

import static com.example.mmm.Obstacle.CROSS_ROTATING_OBSTACLE;
import static com.example.mmm.Obstacle.HORIZONTAL_LAYERED_OBSTACLE;
import static com.example.mmm.Obstacle.HORIZONTAL_OBSTACLE;
import static com.example.mmm.Obstacle.HORIZONTAL_OBSTACLE_SET;
import static com.example.mmm.Obstacle.ROTATING_OBSTACLE;

public class GameUtils {
    public final static float EXT_PADDING = 25.0f;
    public final static float GAP_LAYERED_OBSTACLE = 30.0f;
    //    public final static float MOVE_DOWN_RATE = FRAME_RECT_SPEED; // This is the vertical speed of obstacles.
    public static final float POINTER_RADIUS = 50f;
    public final static int TYPES_OF_OBSTACLES = 5;
    public final static float SCORE_INCREASE_RATE = 0.05f;
    public final static float SCORE_EACH_OBSTACLE = 1;
    public final static float FRAME_SPEED_RATE = 0.01f;
    public final static float MAX_SPEED = 50.0f;
    public static float FRAME_RECT_SPEED = 11.0f;  // Change this to adjust moving speed of background. This is just for the background
    public final static float INITIAL_FRAME_RECT_SPEED = 11.0f;

    /**
     * @return Returns true or false with 50-50 probability.
     */
    public static boolean getRandomSign() {
        return Math.random() < 0.5;
    }

    /**
     * @param probability Probability of true required
     * @return Returns true with given probability.
     */
    public static boolean getRandomSignProbability(double probability) {
        return Math.random() < probability;
    }

    /**
     * @return Returns random type of obstacle (A string representing the name of the type).
     */
    public static String getRandomObstacleType() {
        int rand = (int) new Random().nextInt(5);
        int i = (int) Math.floor(Math.random() * TYPES_OF_OBSTACLES);
        switch (i) {
            case 0:
                return CROSS_ROTATING_OBSTACLE;
            case 1:
                return ROTATING_OBSTACLE;
            case 2:
                return HORIZONTAL_OBSTACLE;
            case 3:
                return HORIZONTAL_OBSTACLE_SET;
            case 4:
                return HORIZONTAL_LAYERED_OBSTACLE;
            default:
                return CROSS_ROTATING_OBSTACLE;
        }
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
