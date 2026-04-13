package com.example.endlessrunner;

import java.util.ArrayList;
import java.util.Random;

public class HethongCNV
{
    public static ArrayList<Integer> obstacleX = new ArrayList<>();
    public static ArrayList<Integer> obstacleY = new ArrayList<>();
    public static ArrayList<String> obstacleType = new ArrayList<>();
    static Random random = new Random();
    static int spawnTimer = 0;
    static int spawnRate = 40;
    static String[] GROUND_TYPES = {"ground1", "ground2", "ground3", "ground4"};
    static String[] AIR_TYPES = {"air1", "air2", "air3", "air4"};
    static int heightLow = 60;
    static int heightHigh = 150;
    static int SPAWN_X = 3000;
    static int REMOVE_X = -300;
    public static void setupManHinh(int width) { SPAWN_X = width + 300; }
    static void spawnCNV()
    {
        int rand = random.nextInt(2);
        if (rand == 0) spawnGroundCNV();
        else spawnAirCNV();
    }

    static void spawnGroundCNV()
    {
        obstacleX.add(SPAWN_X);
        obstacleY.add(0);
        obstacleType.add(GROUND_TYPES[random.nextInt(GROUND_TYPES.length)]);
    }

    static void spawnAirCNV()
    {
        obstacleX.add(SPAWN_X);
        boolean isHigh = random.nextBoolean();
        if (isHigh)
        {
            obstacleY.add(heightHigh);
            obstacleType.add("air_high");
        }
        else
        {
            obstacleY.add(heightLow);
            obstacleType.add(AIR_TYPES[random.nextInt(AIR_TYPES.length)]);
        }
    }

    public static void HethongCNV(int speed)
    {
        spawnTimer++;
        if (spawnTimer >= spawnRate)
        {
            spawnCNV();
            spawnTimer = 0;
            spawnRate = 40 + random.nextInt(41);
        }

        for (int i = 0; i < obstacleX.size(); i++) obstacleX.set(i, obstacleX.get(i) - speed);
        for (int i = 0; i < obstacleX.size(); i++)
        {
            if (obstacleX.get(i) < REMOVE_X)
            {
                obstacleX.remove(i); obstacleY.remove(i); obstacleType.remove(i); i--;
            }
        }
    }
}