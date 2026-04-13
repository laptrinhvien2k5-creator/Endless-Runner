package com.example.endlessrunner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;

public class GameView extends View
{
    private int score = 0;
    private int highScore = 0;
    private Paint scorePaint;
    private Paint highScorePaint;
    private ArrayList<Bitmap> runBitmap = new ArrayList<>();
    private int frameCurrent = 0;
    private int frameCounter = 0;
    private final int FRAME_SPEED = 5;
    private Bitmap jumpUpBitmap;
    private Bitmap jumpDownBitmap;
    private Bitmap slideBitmap;
    private HashMap<String, Bitmap> obstacleBitmaps = new HashMap<>();
    private Bitmap backgroundBitmap;
    private int bgX = 0;
    private final int bgSpeed = 8;
    private boolean isPaused = false;

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context)
    {
        super(context);
        init(context);
    }

    // Nạp tất cả tài nguyên hình ảnh (nhân vật, vật cản, nền) vào bộ nhớ khi game bắt đầu
    private void init(Context context)
    {
        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(64);
        scorePaint.setAntiAlias(true);

        highScorePaint = new Paint();
        highScorePaint.setColor(Color.DKGRAY);
        highScorePaint.setTextSize(40);
        highScorePaint.setAntiAlias(true);

        try
        {
            int[] playerIds = {R.drawable.runsprite1, R.drawable.runsprite2, R.drawable.runsprite3, R.drawable.runsprite4};
            for (int id : playerIds)
            {
                Bitmap b = BitmapFactory.decodeResource(getResources(), id);
                if (b != null) runBitmap.add(Bitmap.createScaledBitmap(b, 200, 200, false));
            }

            jumpUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jump_up);
            if (jumpUpBitmap != null) jumpUpBitmap = Bitmap.createScaledBitmap(jumpUpBitmap, 200, 200, false);
            jumpDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jump_down);
            if (jumpDownBitmap != null) jumpDownBitmap = Bitmap.createScaledBitmap(jumpDownBitmap, 200, 200, false);
            slideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide);
            if (slideBitmap != null) slideBitmap = Bitmap.createScaledBitmap(slideBitmap, 220, 160, false);

            backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

            String[] types = {"ground1", "ground2", "ground3", "ground4", "air1", "air2", "air3", "air4"};
            for (String type : types)
            {
                int id = getResources().getIdentifier(type, "drawable", context.getPackageName());
                if (id != 0)
                {
                    Bitmap b = BitmapFactory.decodeResource(getResources(), id);
                    if (b != null)
                    {
                        obstacleBitmaps.put(type, Bitmap.createScaledBitmap(b, 160, 160, false));
                    }
                }
            }
        }
        catch (Exception e) {}
    }

    // Vẽ liên tục từng khung hình của game
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (backgroundBitmap != null)
        {
            if (backgroundBitmap.getHeight() != getHeight())
            {
                float scale = (float) getHeight() / backgroundBitmap.getHeight();
                int newWidth = (int) (backgroundBitmap.getWidth() * scale);
                backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, newWidth, getHeight(), false);
            }

            int width = backgroundBitmap.getWidth();
            int xPos = bgX;
            while (xPos < getWidth())
            {
                canvas.drawBitmap(backgroundBitmap, xPos, 0, null);
                xPos += width;
            }
            if (!KiemTraVaCham.isGameOver && !isPaused)
            {
                bgX -= bgSpeed;
                if (bgX <= -width) bgX += width;
            }
        }

        Bitmap currentSprite = null;
        int pYOffset = 320;
        if (Player.state.equals("JUMP"))
        {
            currentSprite = (Player.speedY > 0) ? jumpUpBitmap : jumpDownBitmap;
        }
        else if (Player.state.equals("SLIDE"))
        {
            currentSprite = slideBitmap;
            pYOffset = 240;
        }
        else
        {
            if (!runBitmap.isEmpty())
            {
                currentSprite = runBitmap.get(frameCurrent);
                if (!KiemTraVaCham.isGameOver && !isPaused)
                {
                    frameCounter++;
                    if (frameCounter >= FRAME_SPEED)
                    {
                        frameCurrent = (frameCurrent + 1) % runBitmap.size();
                        frameCounter = 0;
                    }
                }
            }
        }

        int pX = Player.getLaneX();
        int pY = getHeight() - pYOffset - Player.y;
        if (currentSprite != null) canvas.drawBitmap(currentSprite, pX, pY, null);

        for (int i = 0; i < HethongCNV.obstacleX.size(); i++)
        {
            int oX = HethongCNV.obstacleX.get(i);
            int oY = getHeight() - 280 - HethongCNV.obstacleY.get(i);
            String type = HethongCNV.obstacleType.get(i);
            Bitmap obsBmp = obstacleBitmaps.get(type);
            if (obsBmp == null && type.equals("air_high")) obsBmp = obstacleBitmaps.get("air1");
            if (obsBmp != null) canvas.drawBitmap(obsBmp, oX, oY, null);
        }

        canvas.drawText("Score: " + score, 50, 100, scorePaint);
        canvas.drawText("Record: " + highScore, 50, 150, highScorePaint);

        if (isPaused)
        {
            Paint pauseOverlayPaint = new Paint();
            pauseOverlayPaint.setColor(Color.argb(100, 0, 0, 0));
            canvas.drawRect(0, 0, getWidth(), getHeight(), pauseOverlayPaint);
            Paint pausedTextPaint = new Paint();
            pausedTextPaint.setColor(Color.WHITE);
            pausedTextPaint.setTextSize(100);
            pausedTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("PAUSED", getWidth() / 2f, getHeight() / 2f, pausedTextPaint);
        }

        if (KiemTraVaCham.isGameOver)
        {
            Paint goP = new Paint();
            goP.setColor(Color.RED);
            goP.setTextSize(120);
            goP.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER", getWidth() / 2f, getHeight() / 2f, goP);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        HethongCNV.setupManHinh(w);
    }

    public void setScore(int score) { this.score = score; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
    public void setPaused(boolean paused) { this.isPaused = paused; invalidate(); }
}