package com.example.endlessrunner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_start);
            
            // Hiển thị kỷ lục điểm cao nhất được lưu trong bộ nhớ máy
            TextView highScoreText = findViewById(R.id.highScoreText);
            if (highScoreText != null)
            {
                SharedPreferences prefs = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
                int highScore = prefs.getInt("highScore", 0);
                highScoreText.setText("MAX SCORE: " + highScore);

                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(40f);
                shape.setColor(Color.parseColor("#80FFF9C4"));
                shape.setStroke(3, Color.parseColor("#FBC02D"));
                
                highScoreText.setBackground(shape);
                highScoreText.setTextColor(Color.parseColor("#5D4037")); 
                highScoreText.setPadding(40, 20, 40, 20); 
            }
            
            // Nút nhấn để chuyển từ màn hình chờ vào màn hình chơi game chính
            Button startButton = findViewById(R.id.startButton);
            if (startButton != null)
            {
                startButton.setOnClickListener(v ->
                {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Đóng màn hình chờ sau khi bắt đầu game
                } );
            }
        }
        catch (Exception e)
        {
            // Phòng trường hợp layout gặp lỗi, vẫn tự động chuyển vào game để không bị treo app
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}