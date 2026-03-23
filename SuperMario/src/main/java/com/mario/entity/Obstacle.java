package com.mario.entity;

import com.mario.util.Background;
import com.mario.util.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 障碍物类
 */
public class Obstacle {
    private int x;
    private int y;
    private ObstacleType type;
    private BufferedImage show = null;
    private Background background = null;

    /**
     * 无参构造
     */
    public Obstacle() {
    }

    /**
     * 创建障碍物对象
     *
     * @param x 障碍物 x 坐标
     * @param y 障碍物 y 坐标
     * @param type 障碍物类型
     * @param background 所属背景
     */
    public Obstacle(int x, int y, ObstacleType type, Background background) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.background = background;
        show = StaticValue.obstacle.get(type.getSpriteIndex());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ObstacleType getType() {
        return type;
    }

    public void setType(ObstacleType type) {
        this.type = type;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public BufferedImage getShow() {
        return show;
    }

    public void setShow(BufferedImage show) {
        this.show = show;
    }
}
