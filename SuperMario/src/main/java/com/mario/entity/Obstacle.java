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

    /**
     * 获取 x 坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 设置 x 坐标
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 获取 y 坐标
     */
    public int getY() {
        return y;
    }

    /**
     * 设置 y 坐标
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * 获取障碍物类型
     */
    public ObstacleType getType() {
        return type;
    }

    /**
     * 设置障碍物类型
     */
    public void setType(ObstacleType type) {
        this.type = type;
    }

    /**
     * 获取所属背景
     */
    public Background getBackground() {
        return background;
    }

    /**
     * 设置所属背景
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * 获取当前显示图片
     */
    public BufferedImage getShow() {
        return show;
    }

    /**
     * 设置当前显示图片
     */
    public void setShow(BufferedImage show) {
        this.show = show;
    }
}
