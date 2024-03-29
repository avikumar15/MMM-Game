package com.example.mmm.game.obstacles;

import androidx.annotation.NonNull;

import com.example.mmm.game.Game;

import static com.example.mmm.game.utils.GameUtils.EXT_PADDING;
import static com.example.mmm.game.utils.GameUtils.GAP_LAYERED_OBSTACLE;
import static com.example.mmm.game.utils.GameUtils.POINTER_RADIUS;
import static com.example.mmm.game.utils.GameUtils.getRandomSign;

public class LayeredHorizontalObjects implements Obstacle {
    static float HORIZONTAL_MOVE_RATE = 15f;
    public static float obstacleHeight = 240.0f, obstacleWidth = 240.0f;
    private float cx, cy;
    private Game game;
    private boolean isAlive;
    private boolean isMovingRight;
    private boolean isInvisible = false;

    public LayeredHorizontalObjects(float cx, float cy, Game game) {
        this.cx = cx;
        this.cy = cy;
        this.game = game;

        isAlive = true;
        isMovingRight = getRandomSign();
    }

    @Override
    public String getObstacleType() {
        return HORIZONTAL_LAYERED_OBSTACLE;
    }

    @Override
    public float getCx() {
        return cx;
    }

    @Override
    public float getCy() {
        return cy;
    }

    public float getObstacleHeight() {
        return obstacleHeight;
    }

    public float getObstacleWidth() {
        return obstacleWidth;
    }

    public float getLeft() {
        return cx - obstacleWidth / 2.0f;
    }

    public float getRight() {
        return cx + obstacleWidth / 2.0f;
    }

    @Override
    public float getTop() {
        return cy - obstacleHeight / 2.0f;
    }

    @Override
    public float getBottom() {
        return cy + obstacleHeight / 2.0f;
    }

    @Override
    public void moveDown() {
        cy += game.moveDownSpeed;
        if (getTop() >= game.getHeight() - EXT_PADDING) {
            isAlive = false;
        }
    }

    @Override
    public void update() {
        if (isMovingRight) {
            cx += HORIZONTAL_MOVE_RATE;
        } else {
            cx -= HORIZONTAL_MOVE_RATE;
        }

        if (cx <= EXT_PADDING + obstacleWidth / 2.0f) {
            isMovingRight = true;
        } else if (cx >= game.getWidth() - EXT_PADDING - obstacleWidth / 2.0f) {
            isMovingRight = false;
        }
    }

    @Override
    public boolean isInside(float x, float y) {

        // condition that the y=coordinates of obstacle and pointer matches

        if (y >= cy - obstacleHeight / 2f - POINTER_RADIUS - obstacleHeight && y <= cy + obstacleHeight / 2f + POINTER_RADIUS + obstacleHeight - GAP_LAYERED_OBSTACLE) {

            // condition if pointer is in safe area
            if ((x >= cx - obstacleWidth / 2f - POINTER_RADIUS) && (x <= cx + obstacleWidth / 2f + POINTER_RADIUS)) {
                return false;
            } else
                return true;
        } else
            return false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public boolean isInvisible() {
        return isInvisible;
    }

    @Override
    public void setInvisible() {
        isInvisible = true;
    }

    @NonNull
    @Override
    public String toString() {
        return "Horizontal Obstacle at cx = " + cx + ", cy = " + cy;
    }
}
