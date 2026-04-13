package com.example.endlessrunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

public class GameEngine
{
    private boolean isRunning = false;
    private boolean isPaused = false;
    private float speed = 15f;
    private final float speedStart = 15f;
    private final float accel = 0.002f;
    private int score = 0;
    private int highScore = 0;
    private int scoreCounter = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private GameView gameView;
    private final int frameDelay = 16;
    private SharedPreferences saveRecord;

    public GameEngine(GameView gameView)
    {
        this.gameView = gameView;
        saveRecord = gameView.getContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        highScore = saveRecord.getInt("highScore", 0);
        gameView.setHighScore(highScore);
    }

    private Runnable gameLoop = new Runnable()
    {
        @Override
        public void run()
        {
            if (!isRunning) return;
            update();
            handler.postDelayed(this, frameDelay);
        }
    };

    private void update()
    {
        speed += accel;
        Player.update();
        HethongCNV.HethongCNV((int)speed);
        KiemTraVaCham.kiemTra(Player.getLaneX(), Player.y, Player.state);

        if (KiemTraVaCham.isGameOver)
        {
            checkHighScore();
            stop();
            gameView.invalidate();
            return;
        }

        scoreCounter++;
        if (scoreCounter >= 3)
        {
            score++;
            scoreCounter = 0;
        }

        gameView.setScore(score);
        gameView.invalidate();
    }

    public void togglePause()
    {
        if (KiemTraVaCham.isGameOver) return;
        isPaused = !isPaused;
        if (isPaused)
        {
            isRunning = false;
            handler.removeCallbacks(gameLoop);
        }
        else
        {
            isRunning = true;
            handler.post(gameLoop);
        }
        gameView.setPaused(isPaused);
    }
    public boolean isPaused() {
        return isPaused;
    }

    private void checkHighScore()
    {
        if (score > highScore)
        {
            highScore = score;
            SharedPreferences.Editor editor = saveRecord.edit();
            editor.putInt("highScore", highScore);
            editor.apply();
            gameView.setHighScore(highScore);
        }
    }

    public void start()
    {
        if (isRunning || isPaused) return;
        isRunning = true;
        handler.post(gameLoop);
    }

    public void stop()
    {
        isRunning = false;
        handler.removeCallbacks(gameLoop);
    }

    public void reset()
    {
        score = 0;
        scoreCounter = 0;
        isPaused = false;
        speed = speedStart; 
        Player.y = 0;
        Player.state = "RUN";
        HethongCNV.obstacleX.clear();
        HethongCNV.obstacleY.clear();
        HethongCNV.obstacleType.clear();
        KiemTraVaCham.reset();
        gameView.setHighScore(highScore);
        gameView.setPaused(false);
    }
}