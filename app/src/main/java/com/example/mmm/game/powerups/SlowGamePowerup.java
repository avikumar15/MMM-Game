package com.example.mmm.game.powerups;

import com.example.mmm.game.Game;

import static com.example.mmm.game.utils.GameUtils.EXT_PADDING;
import static com.example.mmm.game.utils.GameUtils.FRAME_SPEED_RATE;
import static com.example.mmm.game.utils.GameUtils.INITIAL_FRAME_RECT_SPEED;
import static com.example.mmm.game.utils.GameUtils.POWERUP_SPEED_OBSTACLE_SPEED_RATIO;

public class SlowGamePowerup implements Powerup {
    private final static int TIME_DURATION = 250;
    public final static float powerupHeight = 200f, powerupWidth = 200f;
    private float cx, cy;
    private int timePicked;
    private boolean  canPick, isActive, isPicked;
    private Game game;

    public SlowGamePowerup(float cx, float cy, Game game){
        this.cx = cx;
        this.cy = cy;
        this.game = game;

        canPick = true;
        isActive = false;

        timePicked = 0;
    }

    @Override
    public void affectGameSpeed() {
        if (game.moveDownSpeed >= INITIAL_FRAME_RECT_SPEED + FRAME_SPEED_RATE){
            game.moveDownSpeed -= FRAME_SPEED_RATE;
        }
    }

    @Override
    public void moveDown() {
        cy += game.moveDownSpeed * POWERUP_SPEED_OBSTACLE_SPEED_RATIO;
        if (getTop() >= game.getHeight() - EXT_PADDING){
            canPick = false;
        }
    }

    @Override
    public void setActive() {
        isActive = true;
    }

    @Override
    public void setPicked() {
        isPicked = true;
        isActive = true;
    }

    @Override
    public int getTimePicked() {
        return timePicked;
    }

    @Override
    public void updateTimePicked() {
        ++timePicked;
        if (timePicked > TIME_DURATION){
            isActive = false;
        }
    }

    @Override
    public boolean isInside(float x, float y) {
        return false;
    }

    @Override
    public void affectRateOfObstacles() { }

    @Override
    public boolean disableCollisions() { return false; }

    @Override
    public boolean isActive() { return isActive; }

    @Override
    public boolean isPicked() { return isPicked; }

    @Override
    public boolean canPick() { return canPick; }

    @Override
    public int getTimeDuration() { return TIME_DURATION; }

    @Override
    public float getCx() { return cx; }

    @Override
    public float getCy() { return cy; }

    @Override
    public float getTop() { return cy - powerupHeight / 2; }

    @Override
    public float getBottom() { return cy + powerupHeight / 2; }

    @Override
    public String getPowerupType() {
        return SLOW_GAME_POWERUP;
    }
}
