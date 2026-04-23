package com.mario.entity.scene;

import com.mario.constant.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 旗子类（可沿旗杆下落）
 */
public class Flag {
    private int x;  // 旗子 x 坐标
    private int y;  // 旗子 y 坐标
    private BufferedImage show = null;  // 当前显示图片
    private Background background = null;  // 所属背景

    /**
     * 无参构造方法
     */
    public Flag() {
    }

    /**
     * 创建旗子对象
     *
     * @param x 旗子 x 坐标
     * @param y 旗子 y 坐标
     * @param background 所属背景
     */
    public Flag(int x, int y, Background background) {
        this.x = x;
        this.y = y;
        this.background = background;
        this.show = StaticValue.flag;
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

    public BufferedImage getShow() {
        return show;
    }

    public Background getBackground() {
        return background;
    }
}
