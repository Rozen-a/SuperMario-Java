package com.mario.entity;

import com.mario.util.Background;
import com.mario.util.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 城堡类
 */
public class Tower {
    private int x;
    private int y;
    private BufferedImage show = null;
    private Background background = null;

    /**
     * 创建城堡对象
     *
     * @param x 城堡 x 坐标
     * @param y 城堡 y 坐标
     * @param background 所属背景
     */
    public Tower(int x, int y, Background background) {
        this.x = x;
        this.y = y;
        this.background = background;
        this.show = StaticValue.tower;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getShow() {
        return show;
    }

    public Background getBackground() {
        return background;
    }
}
