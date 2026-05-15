package com.mario.entity.creature;

import com.mario.ai.BehaviorEventListener;
import com.mario.entity.scene.Background;
import com.mario.service.MarioCollisionDetector;
import com.mario.service.MarioMovementLogic;
import com.mario.constant.StaticValue;

import java.awt.image.BufferedImage;

/**
 * 马里奥类
 */
public class Mario implements Runnable {
    // 马里奥在场景中的坐标
    private int x;
    private int y;
    // 当前动作状态
    private String status;
    // 当前显示帧
    private BufferedImage show = null;
    // 马里奥所在的游戏背景/关卡数据
    private Background background = new Background();
    // 角色更新线程
    private Thread thread = null;
    // 脚本模式：true 时暂停常规输入驱动移动逻辑（用于过场动画）
    private volatile boolean scriptedMode;

    // 水平/垂直速度
    private int XSpeed;
    private int YSpeed;

    // 表示积分
    private int score = 0;

    // 将移动逻辑与碰撞检测解耦到独立组件
    private final MarioMovementLogic movementLogic;
    private final MarioCollisionDetector collisionDetector;

    /**
     * 创建马里奥对象
     */
    public Mario() {
        movementLogic = new MarioMovementLogic();
        collisionDetector = new MarioCollisionDetector(background);
    }

    /**
     * 创建马里奥对象并设置初始坐标，同时启动角色更新线程
     *
     * @param x 初始横坐标
     * @param y 初始纵坐标
     */
    public Mario(int x, int y) {
        movementLogic = new MarioMovementLogic();
        collisionDetector = new MarioCollisionDetector(background);
        this.x = x;
        this.y = y;
        show = StaticValue.mario_stand_R;
        this.status = "stand-right";
        thread = new Thread(this);
        thread.start();
    }

    /**
     * 角色主循环：按固定时间步执行移动与碰撞更新
     */
    @Override
    public void run() {
        while (true) {
            // 如果不在脚本模式下，则更新马里奥的移动逻辑
            if (!scriptedMode) {
                movementLogic.update(this, collisionDetector);
            }
            try {
                // 每次循环后暂停 30ms，保持固定帧率
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * 设置左移按键状态
     *
     * @param pressed 是否按下
     */
    public void setLeftPressed(boolean pressed) {
        movementLogic.setLeftPressed(this, pressed);
    }

    /**
     * 设置右移按键状态
     *
     * @param pressed 是否按下
     */
    public void setRightPressed(boolean pressed) {
        movementLogic.setRightPressed(this, pressed);
    }

    /**
     * 设置奔跑按键状态
     *
     * @param pressed 是否按下
     */
    public void setRunPressed(boolean pressed) {
        movementLogic.setRunPressed(pressed);
    }

    /**
     * 执行跳跃逻辑
     */
    public void jump() {
        movementLogic.jump(this, collisionDetector);
    }

    /**
     * 设置玩家行为事件监听器
     *
     * @param behaviorEventListener 监听器
     */
    public void setBehaviorEventListener(BehaviorEventListener behaviorEventListener) {
        movementLogic.setBehaviorEventListener(behaviorEventListener);
        collisionDetector.setBehaviorEventListener(behaviorEventListener);
    }

    /**
     * 复位输入与速度状态
     */
    public void resetMotionState() {
        setLeftPressed(false);
        setRightPressed(false);
        setRunPressed(false);
        setXSpeed(0);
        setYSpeed(0);
    }

    /**
     * 停止向左移动并切换为左向站立状态
     */
    public void stopLeft() {
        XSpeed = 0;
        status = "stand-left";
        show = StaticValue.mario_stand_L;
    }

    /**
     * 停止向右移动并切换为右向站立状态
     */
    public void stopRight() {
        XSpeed = 0;
        status = "stand-right";
        show = StaticValue.mario_stand_R;
    }
    
    /**
     * 增加积分。
     *
     * @param delta 增加的分值
     */
    public void addScore(int delta) {
        score += delta;
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
        collisionDetector.setBackground(background);
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public int getXSpeed() {
        return XSpeed;
    }

    public void setXSpeed(int XSpeed) {
        this.XSpeed = XSpeed;
    }

    public int getYSpeed() {
        return YSpeed;
    }

    public void setYSpeed(int YSpeed) {
        this.YSpeed = YSpeed;
    }

    public boolean isScriptedMode() {
        return scriptedMode;
    }

    public void setScriptedMode(boolean scriptedMode) {
        this.scriptedMode = scriptedMode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
