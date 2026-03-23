package com.mario.entity;

import com.mario.util.Background;
import com.mario.util.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 旗杆类
 */
public class Flagpole {
    private int x;
    private int y;
    private BufferedImage show = null;
    private Background background = null;

    /**
     * 创建旗杆对象
     *
     * @param x 旗杆 x 坐标
     * @param y 旗杆 y 坐标
     * @param background 所属背景
     */
    public Flagpole(int x, int y, Background background) {
        this.x = x;
        this.y = y;
        this.background = background;
        this.show = StaticValue.flagpole;
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
