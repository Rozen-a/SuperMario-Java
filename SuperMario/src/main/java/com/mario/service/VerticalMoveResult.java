package com.mario.service;

import com.mario.entity.scene.Obstacle;

/**
 * 垂直位移解析结果。
 *
 * 用于同时返回垂直方向的新坐标、本帧是否发生实际位移，
 * 以及阻挡本次垂直位移的障碍物。
 */
public class VerticalMoveResult {
    private final int y;  // 解析后的 y 坐标
    private final boolean moved;  // 本次垂直位移是否实际发生
    private final Obstacle blockedObstacle;  // 阻挡本次垂直位移的障碍物

    /**
     * 创建垂直位移解析结果。
     *
     * @param y 解析后的 y 坐标
     * @param moved 是否发生实际位移
     * @param blockedObstacle 阻挡本次位移的障碍物
     */
    public VerticalMoveResult(int y, boolean moved, Obstacle blockedObstacle) {
        this.y = y;
        this.moved = moved;
        this.blockedObstacle = blockedObstacle;
    }

    public int getY() {
        return y;
    }

    public boolean isMoved() {
        return moved;
    }

    public Obstacle getBlockedObstacle() {
        return blockedObstacle;
    }
}
