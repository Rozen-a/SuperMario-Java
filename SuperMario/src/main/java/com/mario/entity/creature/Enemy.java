package com.mario.entity.creature;

import com.mario.util.Background;

import java.awt.image.BufferedImage;

public class Enemy implements Runnable {
    // 敌人在场景中的坐标
    private int x;
    private int y;
    // 运动方向，true 为向右，false 为向左
    private boolean toRight;
    // 敌人所在背景
    private Background background;
    // 当前显示帧
    private BufferedImage show;
    // 敌人线程
    private Thread thread;

    /**
     * 无参构造
     */
    public Enemy() {
    }

    /**
     * 创建敌人对象
     *
     * @param x 敌人 x 坐标
     * @param y 敌人 y 坐标
     * @param toRight 敌人运动方向
     * @param background 敌人所在背景
     */
    public Enemy(int x, int y, boolean toRight, Background background) {
        this.x = x;
        this.y = y;
        this.toRight = toRight;
        this.background = background;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
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

    public boolean isToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
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

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
