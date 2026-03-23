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

    // 移动速度
    private int XSpeed;
    private int YSpeed;

    private boolean leftPressed;  // 左方向键是否按下
    private boolean rightPressed;  // 右方向键是否按下
    private boolean runPressed;  // 奔跑键（Shift）是否按下
    private boolean jumping;  // 是否处于起跳/上升阶段
    private boolean faceRight = true;  // 当前朝向，true 为朝右，false 为朝左
    private int jumpFrame;  // 当前跳跃已执行的帧数（用于控制上升持续时间）
    private int runAnimationIndex;  // 跑步动画帧索引（在跑步帧列表中循环）
    private int runAnimationTick;  // 跑步动画节拍计数器（用于控制切帧快慢）

    private static final int NORMAL_SPEED = 4;  // 普通移动速度
    private static final int RUN_SPEED = 7;  // 奔跑移动速度
    private static final int JUMP_SPEED = 10;  // 起跳上升速度
    private static final int GRAVITY = 8;  // 下落重力速度
    private static final int JUMP_MAX_FRAME = 8;  // 跳跃上升最大帧数
    private static final int GROUND_Y = 420;  // 地面 y 坐标（超过该值会被拉回地面）
    private static final int LEFT_BOUND = 0;  // 场景左边界
    private static final int RIGHT_BOUND = 900;  // 场景右边界
    private static final int NORMAL_ANIMATION_INTERVAL = 6;  // 普通移动时每 30 帧切一次动画
    private static final int RUN_ANIMATION_INTERVAL = 3;  // 奔跑时每 10 帧切一次动画

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
     * 线程执行方法，用于持续更新马里奥动作状态
     */
    @Override
    public void run() {
        // 角色逻辑主循环：周期性更新移动和跳跃/下落
        while (true) {
            // 处理左右移动、奔跑速度和水平动画
            updateMove();
            // 处理垂直方向的跳跃上升与重力下落
            updateJumpAndFall();
            try {
                // 固定逻辑帧间隔（约 33 FPS）
                Thread.sleep(30);
            } catch (InterruptedException e) {
                // 收到中断信号时安全退出线程
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * 按给定偏移量移动马里奥（仅更新坐标，不做碰撞判断）
     *
     * @param xOffset x 方向偏移（右正左负）
     * @param yOffset y 方向偏移（下正上负）
     */
    public void move(int xOffset, int yOffset) {
        this.x += xOffset;
        this.y += yOffset;
    }

    /**
     * 更新水平移动与跑动动画
     */
    private void updateMove() {
        // 按住 Shift 时使用奔跑速度，否则使用普通速度
        int speed = runPressed ? RUN_SPEED : NORMAL_SPEED;
        if (leftPressed) {
            // 向左移动：速度取负，朝向改为左
            XSpeed = -speed;
            move(XSpeed, 0);
            faceRight = false;
            // 空中水平移动显示 jump-left，地面显示 move-left
            status = jumping ? "jump-left" : "move-left";
            if (!StaticValue.mario_run_L.isEmpty()) {
                // 奔跑时切帧更快，普通移动切帧更慢
                int interval = runPressed ? RUN_ANIMATION_INTERVAL : NORMAL_ANIMATION_INTERVAL;
                runAnimationTick++;
                if (runAnimationTick >= interval) {
                    runAnimationTick = 0;
                    runAnimationIndex = (runAnimationIndex + 1) % StaticValue.mario_run_L.size();
                }
                show = StaticValue.mario_run_L.get(runAnimationIndex);
            }
        } else if (rightPressed) {
            // 向右移动：速度取正，朝向改为右
            XSpeed = speed;
            move(XSpeed, 0);
            faceRight = true;
            // 空中水平移动显示 jump-right，地面显示 move-right
            status = jumping ? "jump-right" : "move-right";
            if (!StaticValue.mario_run_R.isEmpty()) {
                // 奔跑时切帧更快，普通移动切帧更慢
                int interval = runPressed ? RUN_ANIMATION_INTERVAL : NORMAL_ANIMATION_INTERVAL;
                runAnimationTick++;
                if (runAnimationTick >= interval) {
                    runAnimationTick = 0;
                    runAnimationIndex = (runAnimationIndex + 1) % StaticValue.mario_run_R.size();
                }
                show = StaticValue.mario_run_R.get(runAnimationIndex);
            }
        } else {
            // 无左右输入时水平速度清零
            XSpeed = 0;
            runAnimationTick = 0;
            // 且在地面时切换到站立状态，保持最后朝向
            if (!jumping && y >= GROUND_Y) {
                if (faceRight) {
                    stopRight();
                } else {
                    stopLeft();
                }
            }
        }
        // 水平边界限制：防止角色移出屏幕
        if (x < LEFT_BOUND) {
            x = LEFT_BOUND;
        } else if (x > RIGHT_BOUND) {
            x = RIGHT_BOUND;
        }
    }

    /**
     * 更新跳跃与下落逻辑
     */
    private void updateJumpAndFall() {
        if (jumping && jumpFrame < JUMP_MAX_FRAME) {
            // 跳跃上升阶段：持续给向上的速度直到达到最大上升帧数
            YSpeed = -JUMP_SPEED;
            move(0, YSpeed);
            jumpFrame++;
            status = faceRight ? "jump-right" : "jump-left";
            show = faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L;
        } else {
            // 上升结束后进入下落判断
            jumping = false;
            if (y < GROUND_Y) {
                // 处于空中：应用重力下落
                YSpeed = GRAVITY;
                move(0, YSpeed);
                status = faceRight ? "fall-right" : "fall-left";
                show = faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L;
            } else {
                // 落地：回到地面高度并重置垂直速度与跳跃帧计数
                y = GROUND_Y;
                YSpeed = 0;
                jumpFrame = 0;
            }
        }
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

    /**
     * 设置向左移动按键状态
     *
     * @param pressed 是否按下
     */
    public void setLeftPressed(boolean pressed) {
        this.leftPressed = pressed;
        if (!pressed && !rightPressed && !jumping) {
            stopLeft();
        }
    }

    /**
     * 设置向右移动按键状态
     *
     * @param pressed 是否按下
     */
    public void setRightPressed(boolean pressed) {
        this.rightPressed = pressed;
        if (!pressed && !leftPressed && !jumping) {
            stopRight();
        }
    }

    /**
     * 设置奔跑按键状态
     *
     * @param pressed 是否按下
     */
    public void setRunPressed(boolean pressed) {
        this.runPressed = pressed;
    }

    /**
     * 起跳
     */
    public void jump() {
        if (!jumping && y >= GROUND_Y) {
            jumping = true;
            jumpFrame = 0;
            status = faceRight ? "jump-right" : "jump-left";
            show = faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L;
        }
    }

    /**
     * 向左停止
     */
    public void stopLeft() {
        XSpeed = 0;
        status = "stand-left";
        show = StaticValue.mario_stand_L;
    }

    /**
     * 向右停止
     */
    public void stopRight() {
        XSpeed = 0;
        status = "stand-right";
        show = StaticValue.mario_stand_R;
    }
}
