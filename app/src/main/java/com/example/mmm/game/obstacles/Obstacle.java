package com.example.mmm.game.obstacles;

public interface Obstacle {
    public final static String ROTATING_OBSTACLE = "RotatingObstacle", HORIZONTAL_OBSTACLE = "HorizontalObstacle", CROSS_ROTATING_OBSTACLE = "CrossRotatingObstacle", HORIZONTAL_OBSTACLE_SET = "HorizontalObstacleSet", HORIZONTAL_LAYERED_OBSTACLE = "LayeredHorizontalObjects", MUTUALLY_ATTRACTED_OBSTACLE="MutuallyAttractedObstacles";
    public String getObstacleType();
    public float getCx();
    public float getCy();
    public float getTop();
    public float getBottom();

    /**
     * Increases the y coordinate by move down rate.
     * This is same for all the types of obstacles.
     */
    public void moveDown();

    /**
     * Requires modification of logic for each type of obstacle implemented
     * @param x x coordinate of the pointer
     * @param y y coordinate of the pointer
     * @return Checks whether the pointer lies inside the obstacle.
     */
    public boolean isInside(float x, float y);
    public boolean isAlive();
    public void setAlive(boolean isALive);

    /**
     * Use this only if collisions are disabled through a powerup and an obstacle hit the pointer.
     */
    public void setInvisible();
    public boolean isInvisible();

    /**
     * This depends on the logic of moving the obstacle.
     * eg. Circular motion logic for rotating pair, Changing cx for horizontal movement etc.
     */
    public void update();
}
