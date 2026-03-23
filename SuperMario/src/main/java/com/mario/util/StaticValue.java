package com.mario.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 静态变量类
 */
public class StaticValue {
    // 背景
    public static BufferedImage background1 = null;
    public static BufferedImage background2 = null;

    // 马里奥状态
    public static BufferedImage mario_jump_L = null;
    public static BufferedImage mario_jump_R = null;
    public static BufferedImage mario_stand_L = null;
    public static BufferedImage mario_stand_R = null;
    public static List<BufferedImage> mario_run_L = new ArrayList<>();
    public static List<BufferedImage> mario_run_R = new ArrayList<>();

    // 城堡
    public static BufferedImage tower = null;

    // 旗杆
    public static BufferedImage flagpole = null;

    // 障碍物
    public static List<BufferedImage> obstacle = new ArrayList<>();

    // 蘑菇敌人
    public static List<BufferedImage> mushroom = new ArrayList<>();

    // 食人花敌人
    public static List<BufferedImage> man_eating_flower = new ArrayList<>();

    // 统一的图片加载方法
    private static BufferedImage loadImage(String fileName) throws IOException {
        String resourcePath = "/images/" + fileName;
        try (InputStream inputStream = StaticValue.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return ImageIO.read(inputStream);
        }
    }

    // 初始化图片
    public static void init() {
        try {
            // 加载背景图片
            background1 = loadImage("bg1.png");
            background2 = loadImage("bg2.png");

            // 加载马里奥单图动作图片
            mario_jump_L = loadImage("mario_jump_L.png");
            mario_jump_R = loadImage("mario_jump_R.png");
            mario_stand_L = loadImage("mario_stand_L.png");
            mario_stand_R = loadImage("mario_stand_R.png");

            // 加载城堡图片
            tower = loadImage("tower.png");

            // 加载旗杆图片
            flagpole = loadImage("flagpole.png");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 加载马里奥多图动作图片
        // 向左跑
        for (int i = 1; i <= 2; i++) {
            try {
                mario_run_L.add(loadImage("mario_run" + i + "_L.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 向右跑
        for (int i = 1; i <= 2; i++) {
            try {
                mario_run_R.add(loadImage("mario_run" + i + "_R.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 加载障碍物
        try {
            obstacle.add(loadImage("block_breakable.png"));
            obstacle.add(loadImage("block_unbreakable.png"));
            obstacle.add(loadImage("soil_up.png"));
            obstacle.add(loadImage("soil_base.png"));
            obstacle.add(loadImage("soil_base.png"));
            obstacle.add(loadImage("flag.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 加载蘑菇敌人
        for (int i = 1; i <= 3; i++){
            try {
                mushroom.add(loadImage("mushroom" + i + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 加载食人花敌人
        for (int i = 1; i <= 2; i++){
            try {
                man_eating_flower.add(loadImage("man_eating_flower" + i + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

}
