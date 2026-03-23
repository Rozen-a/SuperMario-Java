package com.mario.entity.creature;

import com.mario.util.Background;
import com.mario.util.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 马里奥类
 */
public class Mario implements Runnable {
    private int x;
    private int y;
    private String status;
    private BufferedImage show = null;
    private Background background = new Background();  // 用于获取障碍物信息
    private Thread thread = null;

    /**
     * 无参构造方法
     */
    public Mario() {
    }

    /**
     * 创建指定坐标的马里奥并启动动作线程
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    public Mario(int x, int y) {
        this.x = x;
        this.y = y;
        show = StaticValue.mario_stand_R;
        this.status = "stand-right";
        thread = new Thread(this);
        thread.start();
    }

    /**
     * 线程执行方法，用于处理马里奥动作
     */
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BufferedImage getShow() {
        return show;
    }

    public void setShow(BufferedImage show) {
        this.show = show;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
