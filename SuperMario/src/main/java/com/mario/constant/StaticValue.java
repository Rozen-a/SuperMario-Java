package com.mario.constant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 静态变量类：
 * 加载图片资源
 */
public class StaticValue {
    // 背景
    public static BufferedImage background1 = null;
    public static BufferedImage background2 = null;
    public static BufferedImage Start_background = null;

    // Logo
    public static BufferedImage logo = null;

    // 返回主菜单图片
    public static BufferedImage home = null;

    // 马里奥动作
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
    // 旗子
    public static BufferedImage flag = null;

    // 障碍物
    public static List<BufferedImage> obstacle = new ArrayList<>();

    // 蘑菇敌人
    public static List<BufferedImage> mushroom = new ArrayList<>();

    // 食人花敌人
    public static List<BufferedImage> man_eating_flower = new ArrayList<>();

    /**
     * 从 classpath 加载图片资源
     *
     * @param fileName 文件名
     * @return 读取到的图片
     */
    private static BufferedImage loadImage(String fileName) {
        String resourcePath = "/images/" + fileName;
        try (InputStream inputStream = StaticValue.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image: " + resourcePath, e);
        }
    }

    /**
     * 初始化并缓存游戏图片资源
     */
    public static void init() {
        // 允许重复调用 init，先清空列表避免重复追加
        mario_run_L.clear();
        mario_run_R.clear();
        obstacle.clear();
        mushroom.clear();
        man_eating_flower.clear();

        // 加载背景图片
        background1 = loadImage("bg1.png");
        background2 = loadImage("bg2.png");
        Start_background = loadImage("Start_bg.png");

        // 加载Logo图片
        logo = loadImage("logo.png");

        // 加载返回主菜单图片
        home = loadImage("home.png");

        // 加载马里奥动作图片
        mario_jump_L = loadImage("mario_jump_L.png");
        mario_jump_R = loadImage("mario_jump_R.png");
        mario_stand_L = loadImage("mario_stand_L.png");
        mario_stand_R = loadImage("mario_stand_R.png");
        for (int i = 1; i <= 2; i++) {
            mario_run_L.add(loadImage("mario_run" + i + "_L.png"));
        }
        for (int i = 1; i <= 2; i++) {
            mario_run_R.add(loadImage("mario_run" + i + "_R.png"));
        }
        

        // 加载城堡图片
        tower = loadImage("tower.png");

        // 加载旗杆图片
        flagpole = loadImage("flagpole.png");
        // 加载旗子图片
        flag = loadImage("flag.png");


        // 加载障碍物
        obstacle.add(loadImage("block_breakable.png"));
        obstacle.add(loadImage("block_unbreakable.png"));
        obstacle.add(loadImage("soil_up.png"));
        obstacle.add(loadImage("soil_base.png"));
        for (int i = 1; i <= 4; i++){
            obstacle.add(loadImage("pipe" + i + ".png"));
        }

        // 加载蘑菇敌人
        mushroom.add(loadImage("mushroom_squished.png"));
        mushroom.add(loadImage("mushroom_stand.png"));
        mushroom.add(loadImage("mushroom_walk1.png"));
        mushroom.add(loadImage("mushroom_walk2.png"));

        // 加载食人花敌人
        for (int i = 1; i <= 2; i++){
            man_eating_flower.add(loadImage("man_eating_flower" + i + ".png"));
        }
    }

}
