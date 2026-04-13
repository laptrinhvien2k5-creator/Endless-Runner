package com.example.endlessrunner;

public class KiemTraVaCham
{
    public static boolean isGameOver = false;

    static int getCNVWidth(String loai) { return 80; }
    static int getCNVHeight(String loai)
    {
        if (loai.equals("air_high")) return 500;
        return 70;
    }

    static boolean aabbVaCham(int pX, int pY, int pW, int pH, int oX, int oY, int oW, int oH)
    {
        return pX < oX + oW && pX + pW > oX && pY < oY + oH && pY + pH > oY;
    }

    public static void kiemTra(int playerX, int playerY, String playerState)
    {
        // Hitbox nhân vật
        int pX = playerX + 20; 
        int pW = 50;
        int pY = playerY;
        int pH = 200;
        if (playerState.equals("SLIDE"))
        {
            pW = 100;
            pH = 50;
        }

        for (int i = 0; i < HethongCNV.obstacleX.size(); i++)
        {
            int oX = HethongCNV.obstacleX.get(i);
            int oY = HethongCNV.obstacleY.get(i);
            String loai = HethongCNV.obstacleType.get(i);
            int oW = getCNVWidth(loai);
            int oH = getCNVHeight(loai);

            if (aabbVaCham(pX, pY, pW, pH, oX, oY, oW, oH))
            {
                isGameOver = true;
                return;
            }
        }
    }

    public static void reset() {
        isGameOver = false;
    }
}