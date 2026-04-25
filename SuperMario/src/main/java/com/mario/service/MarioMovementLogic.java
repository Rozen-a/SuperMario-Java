package com.mario.service;

import com.mario.entity.creature.Mario;
import com.mario.constant.StaticValue;
import com.mario.util.MusicPlayer;

/**
 * 马里奥移动逻辑（移动、奔跑、跳跃、下落）
 */
public class MarioMovementLogic {
    private static final int NORMAL_SPEED = 4;  // 普通移动速度
    private static final int RUN_SPEED = 7;  // 奔跑速度
    private static final int JUMP_SPEED = 10;  // 起跳上升速度
    private static final int GRAVITY = 8;  // 下落速度
    private static final int JUMP_MAX_FRAME = 8;  // 上升阶段最大帧数
    private static final int GROUND_Y = 420;  // 地面高度
    private static final int LEFT_BOUND = 0;  // 左边界
    private static final int RIGHT_BOUND = 900;  // 右边界
    private static final int NORMAL_ANIMATION_INTERVAL = 6;  // 普通移动动画切帧间隔
    private static final int RUN_ANIMATION_INTERVAL = 3;  // 奔跑动画切帧间隔

    private boolean leftPressed;  // 左键状态
    private boolean rightPressed;  // 右键状态
    private boolean runPressed;  // 奔跑键状态（Shift）
    private boolean jumping;  // 是否在起跳上升阶段
    private boolean faceRight = true;  // 朝向
    private int jumpFrame;  // 当前上升已执行帧数
    private int runAnimationIndex;  // 跑步动画帧索引
    private int runAnimationTick;  // 跑步动画节拍计数

    /**
     * 单帧更新入口：按顺序处理水平移动与垂直运动
     *
     * @param mario 马里奥对象
     * @param detector 碰撞检测器
     */
    public void update(Mario mario, MarioCollisionDetector detector) {
        // 每帧先处理水平逻辑，再处理垂直逻辑
        updateMove(mario, detector);
        updateJumpAndFall(mario, detector);
    }

    /**
     * 设置左键状态
     *
     * @param mario 马里奥对象
     * @param pressed 是否按下
     */
    public void setLeftPressed(Mario mario, boolean pressed) {
        leftPressed = pressed;
        // 如果左键没有按下，右键没有按下，并且没有在跳跃，则停止左移
        if (!pressed && !rightPressed && !jumping) {
            mario.stopLeft();
        }
    }

    /**
     * 设置右键状态
     *
     * @param mario 马里奥对象
     * @param pressed 是否按下
     */
    public void setRightPressed(Mario mario, boolean pressed) {
        rightPressed = pressed;
        // 如果右键没有按下，左键没有按下，并且没有在跳跃，则停止右移
        if (!pressed && !leftPressed && !jumping) {
            mario.stopRight();
        }
    }

    /**
     * 设置奔跑键（Shift）状态
     *
     * @param pressed 是否按下
     */
    public void setRunPressed(boolean pressed) {
        runPressed = pressed;
    }

    /**
     * 执行起跳动作
     *
     * @param mario 马里奥对象
     * @param detector 碰撞检测器
     */
    public void jump(Mario mario, MarioCollisionDetector detector) {
        // 仅允许在地面或站在障碍物上时起跳
        if (!jumping && (mario.getY() >= GROUND_Y || detector.isStandingOnObstacle(mario.getX(), mario.getY()))) {
            MusicPlayer.playSound("Jump");
            jumping = true;
            jumpFrame = 0;
            mario.setStatus(faceRight ? "jump-right" : "jump-left");
            mario.setShow(faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L);
        }
    }

    /**
     * 更新水平移动、朝向与移动动画
     *
     * @param mario 马里奥对象
     * @param detector 碰撞检测器
     */
    private void updateMove(Mario mario, MarioCollisionDetector detector) {
        // 按住 Shift 时切换到奔跑速度
        int speed = runPressed ? RUN_SPEED : NORMAL_SPEED;
        // 如果左键按下，则向左移动
        if (leftPressed) {
            mario.setXSpeed(-speed);
            // 水平位移通过碰撞检测器解析，防止穿透障碍
            int nextX = detector.resolveHorizontalMove(mario.getX(), mario.getY(), mario.getXSpeed());
            mario.setX(nextX);
            faceRight = false;
            mario.setStatus(jumping ? "jump-left" : "move-left");
            if (!StaticValue.mario_run_L.isEmpty()) {
                // 奔跑时切帧更快，普通移动更慢
                int interval = runPressed ? RUN_ANIMATION_INTERVAL : NORMAL_ANIMATION_INTERVAL;
                runAnimationTick++;
                if (runAnimationTick >= interval) {
                    runAnimationTick = 0;
                    runAnimationIndex = (runAnimationIndex + 1) % StaticValue.mario_run_L.size();
                }
                mario.setShow(StaticValue.mario_run_L.get(runAnimationIndex));
            }
        // 如果右键按下，则向右移动
        } else if (rightPressed) {
            mario.setXSpeed(speed);
            // 水平位移通过碰撞检测器解析，防止穿透障碍
            int nextX = detector.resolveHorizontalMove(mario.getX(), mario.getY(), mario.getXSpeed());
            mario.setX(nextX);
            faceRight = true;
            mario.setStatus(jumping ? "jump-right" : "move-right");
            if (!StaticValue.mario_run_R.isEmpty()) {
                // 奔跑时切帧更快，普通移动更慢
                int interval = runPressed ? RUN_ANIMATION_INTERVAL : NORMAL_ANIMATION_INTERVAL;
                runAnimationTick++;
                if (runAnimationTick >= interval) {
                    runAnimationTick = 0;
                    runAnimationIndex = (runAnimationIndex + 1) % StaticValue.mario_run_R.size();
                }
                mario.setShow(StaticValue.mario_run_R.get(runAnimationIndex));
            }
        } else {
            // 无左右输入时停下，并重置动画节拍
            mario.setXSpeed(0);
            runAnimationTick = 0;
            if (!jumping && mario.getY() >= GROUND_Y) {
                if (faceRight) {
                    mario.stopRight();
                } else {
                    mario.stopLeft();
                }
            }
        }

        // 边界限制，防止角色移出可视区域
        if (mario.getX() < LEFT_BOUND) {
            mario.setX(LEFT_BOUND);
        } else if (mario.getX() > RIGHT_BOUND) {
            mario.setX(RIGHT_BOUND);
        }
    }

    /**
     * 更新跳跃上升与下落逻辑
     *
     * @param mario 马里奥对象
     * @param detector 碰撞检测器
     */
    private void updateJumpAndFall(Mario mario, MarioCollisionDetector detector) {
        // 如果正在跳跃，并且跳跃帧数小于跳跃最大帧数，则进行跳跃上升
        if (jumping && jumpFrame < JUMP_MAX_FRAME) {
            // 上升阶段：向上移动，撞到障碍时提前结束上升
            mario.setYSpeed(-JUMP_SPEED);
            // 解析垂直移动后的可达位置及移动结果
            VerticalMoveResult result = detector.resolveVerticalMove(mario.getX(), mario.getY(), mario.getYSpeed());
            mario.setY(result.getY());
            
            if (result.isMoved()) {
                // 本帧成功向上移动：累计上升帧数，用于限制跳跃高度
                jumpFrame++;
                // 根据朝向更新状态与贴图
                mario.setStatus(faceRight ? "jump-right" : "jump-left");
                mario.setShow(faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L);
            } else {
                // 如果跳跃被障碍阻挡，则结束跳跃
                detector.handleHeadHit(mario, result);
                jumping = false;
                mario.setYSpeed(0);
                jumpFrame = JUMP_MAX_FRAME;
            }
            return;
        }

        // 上升结束后进入下落判断
        jumping = false;
        if (mario.getY() < GROUND_Y && !detector.isStandingOnObstacle(mario.getX(), mario.getY())) {
            // 空中且脚下无支撑时应用重力
            mario.setYSpeed(GRAVITY);
            // 解析垂直移动后的可达位置及移动结果
            VerticalMoveResult result = detector.resolveVerticalMove(mario.getX(), mario.getY(), mario.getYSpeed());
            // 更新马里奥的纵坐标
            mario.setY(result.getY());
            // 如果本帧成功向下移动，则更新状态与贴图
            if (result.isMoved()) {
                mario.setStatus(faceRight ? "fall-right" : "fall-left");
                mario.setShow(faceRight ? StaticValue.mario_jump_R : StaticValue.mario_jump_L);
            } else {
                // 下落被障碍阻挡，视为落地
                mario.setYSpeed(0);
                jumpFrame = 0;
                onLand(mario);
            }
        } else {
            // 落地后校正高度并重置垂直状态
            if (mario.getY() > GROUND_Y) {
                mario.setY(GROUND_Y);
            }
            mario.setYSpeed(0);
            jumpFrame = 0;
            onLand(mario);
        }
    }

    /**
     * 落地后收敛到站立状态（若仍有水平输入则保持移动流程）
     *
     * @param mario 马里奥对象
     */
    private void onLand(Mario mario) {
        // 仍按住左右键时，由下一帧 updateMove 继续维持移动动画
        if (leftPressed || rightPressed) {
            return;
        }
        if (faceRight) {
            mario.stopRight();
        } else {
            mario.stopLeft();
        }
    }
}
