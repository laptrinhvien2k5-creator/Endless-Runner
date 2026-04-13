package com.example.endlessrunner;

public class Player
{
    static int lane = 1; 
    static int x = 100;
    public static int y = 0;
    public static String state = "RUN";
    public static int speedY = 0;
    static int gravity = -1;
    static int groundY = 0;
    static int jumpForce = 20;
    static int slideTimer = 0;
    static final int slideTiming = 30;

    public static void jump()
    {
        if (y == groundY) 
        {
            state = "JUMP";
            speedY = jumpForce;
        }
    }

    public static void slide()
    {
        if (y == groundY)
        {
            state = "SLIDE";
            slideTimer = slideTiming;
        }
    }

    public static void run()
    {
        if (y == groundY)
        {
            state = "RUN";
        }
    }

    public static void update()
    {
        if (state.equals("JUMP"))
        {
            y += speedY;
            speedY += gravity;
            if (y <= groundY)
            {
                y = groundY;
                speedY = 0;
                state = "RUN";
            }
        }
        
        if (state.equals("SLIDE"))
        {
            slideTimer--;
            if (slideTimer <= 0)
            {
                state = "RUN";
            }
        }
    }

    static int getLaneX()
    {
        if (lane == 0) return 100;
        if (lane == 1) return 200;
        return 300;
    }
}