package com.example.endlessrunner;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private GameView gameView;
    private GameEngine gameEngine;
    private float startY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        gameView = new GameView(this);
        root.addView(gameView);

        ImageButton pauseBtn = new ImageButton(this);
        pauseBtn.setImageResource(android.R.drawable.ic_media_pause);
        
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(30);
        shape.setColor(Color.argb(100, 0, 0, 0));
        pauseBtn.setBackground(shape);
        
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(120, 120);
        params.gravity = Gravity.TOP | Gravity.END;
        params.setMargins(0, 30, 30, 0);
        pauseBtn.setLayoutParams(params);

        pauseBtn.setOnClickListener(v ->
        {
            if (gameEngine != null)
            {
                gameEngine.togglePause();
                if (gameEngine.isPaused())
                {
                    pauseBtn.setImageResource(android.R.drawable.ic_media_play);
                }
                else
                {
                    pauseBtn.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        } );

        root.addView(pauseBtn);
        setContentView(root);

        gameEngine = new GameEngine(gameView);
        gameEngine.start();
    }

    // Nhận diện cử chỉ
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            startY = event.getY();
            if (KiemTraVaCham.isGameOver)
            {
                gameEngine.reset();
                gameEngine.start();
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            float deltaY = event.getY() - startY;
            if (!KiemTraVaCham.isGameOver && (gameEngine != null && !gameEngine.isPaused()))
            {
                if (deltaY < -80) Player.jump();
                else if (deltaY > 80) Player.slide();
                else if (Math.abs(deltaY) < 20) Player.jump();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(gameEngine!=null) gameEngine.stop();
    }

    @Override
    protected void onResume()
    {
        super.onResume(); 
        if(gameEngine!=null && !gameEngine.isPaused()) gameEngine.start(); 
    }
}