package com.mario.service;

import com.mario.entity.creature.Mario;
import com.mario.entity.scene.Obstacle;
import com.mario.entity.scene.Background;
import com.mario.enums.ObstacleType;
import com.mario.util.MusicPlayer;

import java.util.List;

/**
 * 马里奥碰撞检测器
 */
public class MarioCollisionDetector {
    private static final int DEFAULT_MARIO_WIDTH = 25;  // 固定碰撞盒宽度，避免动画帧尺寸差异带来抖动
    private static final int DEFAULT_MARIO_HEIGHT = 30;  // 固定碰撞盒高度

    private Background background;  // 当前场景背景（提供障碍物列表）

    public MarioCollisionDetector(Background background) {
        this.background = background;
    }

    /**
     * 更新碰撞检测所使用的背景对象
     *
     * @param background 当前场景背景
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * 解析水平移动后的可达位置
     *
     * @param x 当前 x 坐标
     * @param y 当前 y 坐标
     * @param offsetX 期望水平位移（右正左负）
     * @return 经过碰撞修正后的 x 坐标
     */
    public int resolveHorizontalMove(int x, int y, int offsetX) {
        int targetX = x + offsetX;
        // 目标点无碰撞，直接到达
        if (canStayAt(targetX, y)) {
            return targetX;
        }
        // 发生碰撞时逐像素逼近，停在障碍物边缘
        int step = offsetX > 0 ? 1 : -1;
        int current = x;
        while (current != targetX) {
            int next = current + step;
            if (!canStayAt(next, y)) {
                break;
            }
            current = next;
        }
        return current;
    }

    /**
     * 解析垂直移动后的可达位置及移动结果
     *
     * @param x 当前 x 坐标
     * @param y 当前 y 坐标
     * @param offsetY 期望垂直位移（下正上负）
     * @return 垂直位移解析结果
     */
    public VerticalMoveResult resolveVerticalMove(int x, int y, int offsetY) {
        int targetY = y + offsetY;
        // 目标点无碰撞，直接到达
        if (canStayAt(x, targetY)) {
            return new VerticalMoveResult(targetY, true, null);
        }

        // 发生碰撞时逐像素逼近，停在障碍物边缘
        int step = offsetY > 0 ? 1 : -1;
        int current = y;
        while (current != targetY) {
            int next = current + step;
            if (!canStayAt(x, next)) {
                break;
            }
            current = next;
        }
        return new VerticalMoveResult(current, current != y, findFirstCollidingObstacle(x, current + step));
    }

    /**
     * 处理马里奥头顶碰到可破坏方块
     *
     * @param mario 马里奥对象
     * @param verticalResult 垂直位移结果
     */
    public void handleHeadHit(Mario mario, VerticalMoveResult verticalResult) {
        // 检查参数是否为空
        if (mario == null || verticalResult == null) {
            return;
        }
        // 只有跳跃上升阶段顶到头部障碍时，才允许破坏可破坏方块。
        if (mario.getYSpeed() >= 0) {
            return;
        }
        // 检查是否碰到可破坏方块
        Obstacle obstacle = verticalResult.getBlockedObstacle();
        if (obstacle == null || obstacle.getType() != ObstacleType.BREAKABLE_BLOCK) {
            return;
        }
        // 从背景中移除该可破坏方块，增加分数
        List<Obstacle> obstacles = background == null ? null : background.getObstacles();
        if (obstacles == null) {
            return;
        }
        if (obstacles.remove(obstacle)) {
            MusicPlayer.playSound("BreakBlock");
            mario.addScore(1);
        }
    }

    /**
     * 判断马里奥脚下是否有可站立障碍物
     *
     * @param x 当前 x 坐标
     * @param y 当前 y 坐标
     * @return true 表示脚下有支撑，false 表示脚下悬空
     */
    public boolean isStandingOnObstacle(int x, int y) {
        // 检测脚下 1 像素位置是否发生碰撞
        return !canStayAt(x, y + 1);
    }

    /**
     * 判断目标位置是否可站立（与障碍物不重叠）
     *
     * @param targetX 目标 x 坐标
     * @param targetY 目标 y 坐标
     * @return true 表示可站立，false 表示发生碰撞
     */
    private boolean canStayAt(int targetX, int targetY) {
        return findFirstCollidingObstacle(targetX, targetY) == null;
    }

    /**
     * 查找与目标位置发生重叠的第一个障碍物
     * 
     * @param targetX 目标 x 坐标
     * @param targetY 目标 y 坐标
     * @return 第一个重叠障碍物；若不存在则返回 null
     */
    private Obstacle findFirstCollidingObstacle(int targetX, int targetY) {
        return findFirstOverlappingObstacle(targetX, targetY, DEFAULT_MARIO_WIDTH, DEFAULT_MARIO_HEIGHT);
    }

    /**
     * 查找与指定矩形发生重叠的第一个障碍物
     *
     * @param targetX 目标矩形左上角 x 坐标
     * @param targetY 目标矩形左上角 y 坐标
     * @param targetWidth 目标矩形宽度
     * @param targetHeight 目标矩形高度
     * @return 第一个重叠障碍物；若不存在则返回 null
     */
    private Obstacle findFirstOverlappingObstacle(int targetX, int targetY, int targetWidth, int targetHeight) {
        // 背景或障碍物为空时，无碰撞
        if (background == null) {
            return null;
        }
        List<Obstacle> obstacles = background.getObstacles();
        if (obstacles == null || obstacles.isEmpty()) {
            return null;
        }

        // 遍历所有障碍物，查找与目标位置重叠的第一个障碍物
        for (Obstacle obstacle : obstacles) {
            // 跳过无效障碍物
            if (obstacle == null || obstacle.getShow() == null) {
                continue;
            }
            int obstacleX = obstacle.getX();
            int obstacleY = obstacle.getY();
            int obstacleWidth = obstacle.getShow().getWidth();
            int obstacleHeight = obstacle.getShow().getHeight();
            // 检查目标位置是否与障碍物重叠
            if (isRectOverlap(targetX, targetY, targetWidth, targetHeight,
                    obstacleX, obstacleY, obstacleWidth, obstacleHeight)) {
                return obstacle;  // 返回第一个重叠的障碍物
            }
        }
        return null;
    }

    /**
     * 轴对齐矩形重叠检测（AABB）
     *
     * @return true 表示两个矩形重叠
     */
    private boolean isRectOverlap(int x1, int y1, int w1, int h1,
                                  int x2, int y2, int w2, int h2) {
        // AABB：x 轴与 y 轴都重叠时才算碰撞
        // 两个矩形在 x 轴方向有交集：x1 的左边在 x2 的右边左侧，且 x1 的右边在 x2 的左边右侧
        // 两个矩形在 y 轴方向有交集：y1 的上边在 y2 的下边上侧，且 y1 的下边在 y2 的上边下侧
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
}
