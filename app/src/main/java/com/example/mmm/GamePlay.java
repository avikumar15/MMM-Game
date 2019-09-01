package com.example.mmm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.mmm.Obstacle.HORIZONTAL_OBSTACLE;

public class GamePlay extends View {

    public static final float POINTER_RADIUS = 60;
    int scoreInt=0;
    private Paint obstaclePaint = new Paint(), brush = new Paint(), paint = new Paint(), backgroundPaint = new Paint(), textPaint = new Paint();
    private float x,y;
    private float height, width;
    float halfSideLength;
    float incrementVariable=0;

    private Path gameplayPath;
    float fingerX, fingerY;
    private boolean started = false, ended = false, gameOver = false;
    Context mContext;
    private Bitmap startScreen, gameOverScreen, startScreenResized, gameOverScreenResized;
    private Game game;

    public int maxFrames = 4;  // Increase this to make size of each background bitmap to reduce
                               // ie. make it look like cropped and decrease to achieve the opposite.
    private Drawable frameDrawable;
    public float frameHeight;
    private List<FrameRect> frameRects = new ArrayList<>();

    private final static String TAG = "GamePlay";

    public GamePlay(Context context, float width, float height) {
        super(context);

        this.width = width;
        this.height = height;

        if (maxFrames > 0) {
            frameHeight = height / (maxFrames - 1);
        }
        else{
            frameHeight = height;
        }

        obstaclePaint.setColor(getResources().getColor(R.color.colorAccent));
   //     brush.setColor(getResources().getColor(R.color.ball_color));
   //     backgroundPaint.setColor(getResources().getColor(R.color.background_black));
        textPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        textPaint.setTextSize(10.0f);

        startScreen = BitmapFactory.decodeResource(getResources(), R.drawable.start_screen);
        gameOverScreen = BitmapFactory.decodeResource(getResources(), R.drawable.game_over_screen);

        startScreenResized = Bitmap.createScaledBitmap(startScreen, (int) width, (int) height, false);
        gameOverScreenResized = Bitmap.createScaledBitmap(gameOverScreen, (int) width, (int) height, false);

        x = 0;
        y = height/2;

        Log.d(TAG, "Height = " + height + ", width = " + width);

        mContext = context;
    }

    public void start(){
        started = true;
        ended = false;
        gameOver = false;
        game = new Game(width, height);
        gameplayPath = new Path();

        for (int i = maxFrames - 2; i >= 0; --i){
            frameRects.add(new FrameRect(frameHeight * i, frameHeight * (i + 1), height));
        }

        Log.d(TAG, "Started!");
        invalidate();
    }

    private void setGameOver(){
        gameOver = true;
        ended = true;
        started = false;
        Log.d(TAG, "Game Over");

    }

    private void showStartingScreen(Canvas canvas){
        canvas.drawBitmap(startScreenResized, 0, 0, paint);
        invalidate();
    }

    private void showGameOverScreen(Canvas canvas){
        canvas.drawBitmap(gameOverScreenResized, 0, 0, paint);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (!started && !ended){
            showStartingScreen(canvas);
        }
        else if (ended){
            showGameOverScreen(canvas);
        }
        else {
        //    canvas.drawRect(0, 0, width, height, backgroundPaint);
//            halfSideLength = getHeight() / 30f;
            //    canvas.drawPath(gameplayPath,brush);
            drawFrameRect(canvas);
            canvas.drawCircle(fingerX, fingerY, POINTER_RADIUS, brush);
            drawObstacles(canvas);

//            canvas.drawRect(x - halfSideLength + incrementVariable, y - halfSideLength + incrementVariable, x + halfSideLength + incrementVariable, y + halfSideLength + incrementVariable, obstaclePaint);

//            if (fingerX >= x - halfSideLength + incrementVariable && fingerX <= x + halfSideLength + incrementVariable && fingerY >= y - halfSideLength + incrementVariable && fingerY <= y + halfSideLength + incrementVariable) {
//                Toast.makeText(mContext, "Game Over", Toast.LENGTH_SHORT).show();
//            }
//
//            if (x + halfSideLength + incrementVariable <= 3 * getWidth() / 4f) {
//                incrementVariable += 5;
//            }
        }
        scoreInt++;
        canvas.drawText("Current Score - "+scoreInt,width/4f,height/8f,textPaint);      //not working work on it
        System.out.println("Current Score - "+scoreInt);        // not working work on it
        invalidate();
    }

    private void drawObstacles(Canvas canvas){
        List<Obstacle> obstacles = game.getObstacles();
        for (Obstacle obstacle : obstacles){
            if (obstacle.getObstacleType().equals(Obstacle.ROTATING_OBSTACLE)){
                RotatingObstacle rotatingObstacle = (RotatingObstacle) obstacle;
                canvas.drawCircle(rotatingObstacle.getObstacleCx1(), rotatingObstacle.getObstacleCy1(), rotatingObstacle.getObstacleRadius(), obstaclePaint);
                canvas.drawCircle(rotatingObstacle.getObstacleCx2(), rotatingObstacle.getObstacleCy2(), rotatingObstacle.getObstacleRadius(), obstaclePaint);
            }
            else if (obstacle.getObstacleType().equals(HORIZONTAL_OBSTACLE)){
                HorizontalObstacle horizontalObstacle = (HorizontalObstacle) obstacle;
                canvas.drawRect(horizontalObstacle.getLeft(), horizontalObstacle.getTop(), horizontalObstacle.getRight(), horizontalObstacle.getBottom(), obstaclePaint);
            }
            if (obstacle.isInside(fingerX, fingerY)){
                Log.d(TAG, "Game Over! Ball inside obstacle: " + obstacle);
                setGameOver();
                invalidate();
                break;
            }
        }
        game.update();
        invalidate();
    }

    private void drawFrameRect(Canvas canvas){
        if (frameRects == null || frameRects.size() == 0){
            return;
        }
        float frameTop = frameRects.get(frameRects.size() - 1).getTop();
        if (frameTop > 0){
            frameRects.add(new FrameRect(frameTop - frameHeight, frameTop, height));
        }
        if (!frameRects.get(0).isInScreen()){
            frameRects.remove(0);
        }
        for (FrameRect frameRect : frameRects){
            try {
                frameDrawable = ContextCompat.getDrawable(getContext(), R.drawable.tile);
                frameDrawable.setBounds(0, (int) frameRect.getTop(), (int) width, (int) frameRect.getBottom());
                frameDrawable.draw(canvas);
                frameDrawable.invalidateSelf();
            }
            catch (Exception e){
                Log.d(TAG, "Exception caught", e);
            }
            frameRect.update();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        performClick();
        float cx = event.getX();
        float cy = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : {
                if (!started && ended){
                    Log.d(TAG, "Going to starting screen");

                //    ended = false;

                    Intent intent = new Intent(mContext,MainActivity.class);
                    mContext.startActivity(intent);

                    invalidate();
                    return false;
                }

                fingerX=cx;
                fingerY=cy;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!started && !ended){
                    Log.d(TAG, "Action Move");
                    start();
                    invalidate();
                    return true;
                }
                fingerX=cx;
                fingerY=cy;
                break;
            }
            default: {
                if (!started && !ended){
                    return true;
                }
                if (!gameOver) {
                    // add game over code here
                    setGameOver();
                    invalidate();
                    return false;
                }
            }
        }

        invalidate();
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}