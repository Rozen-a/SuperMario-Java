package com.mario.ai;

import com.mario.entity.creature.Mario;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * 玩家行为跟踪器
 *
 * 负责记录最近一段时间的位置轨迹和关键行为
 * 并输出困难识别所需的统计特征
 */
public class PlayerBehaviorTracker {
    private static final long WINDOW_FRAMES = 240;  // 滚动窗口大小
    private static final int STUCK_PROGRESS_THRESHOLD = 60;  // 卡住进度阈值（如果最近窗口内横向推进距离不超过 60 像素，就认为玩家可能在原地徘徊。）
    private static final int DIRECTION_CHANGE_MIN_DELTA = 3;  // 方向变化最小阈值

    private final Deque<PositionSample> recentPositions = new ArrayDeque<>();  // 记录最近窗口内的坐标采样点
    private final Deque<ActionSample> recentActions = new ArrayDeque<>();  // 记录最近窗口内的关键行为事件
    private final Deque<Long> directionChangeFrames = new ArrayDeque<>();  // 记录最近窗口内发生左右折返的帧号
    private final Map<String, Integer> areaDeathCounts = new HashMap<>();  // 记录窗口内死亡次数
    private final Map<String, Integer> areaRestartCounts = new HashMap<>();  // 记录窗口内重新开始次数

    private long currentFrame;  // 当前帧号
    private int currentLevelIndex;  // 当前关卡索引
    private int currentAreaId = 1;  // 当前区域编号
    private int currentX;  // 当前横坐标
    private int currentY;  // 当前纵坐标
    private Integer lastX;  // 上一横坐标，用于判断本帧是向左还是向右移动
    private int lastDirection;  // 上一段有效移动方向：1 表示向右，-1 表示向左，0 表示未确定


    /**
     * 开始新的一轮游戏会话
     */
    public synchronized void startNewSession() {
        areaDeathCounts.clear();
        areaRestartCounts.clear();
        clearRollingState();
        currentFrame = 0;
        currentLevelIndex = 0;
        currentAreaId = 1;
        currentX = 0;
        currentY = 0;
    }

    /**
     * 进入新关卡时重置滚动窗口数据
     *
     * @param levelIndex 当前关卡索引
     */
    public synchronized void onLevelStart(int levelIndex) {
        currentLevelIndex = levelIndex;
        clearRollingState();
        currentFrame = 0;
    }

    /**
     * 每帧采样一次玩家位置
     *
     * @param mario 马里奥对象
     * @param levelIndex 当前关卡索引
     * @param areaId 当前区域编号
     */
    public synchronized void onFrame(Mario mario, int levelIndex, int areaId) {
        if (mario == null) {
            return;
        }
        currentFrame++;
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = mario.getX();
        currentY = mario.getY();

        updateOscillation(currentX);
        recentPositions.addLast(new PositionSample(currentFrame, currentX, currentY));
        trimExpiredSamples();
    }

    /**
     * 记录一次成功起跳
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onJump(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.JUMP, x, y, areaId);
    }

    /**
     * 记录一次跳跃受阻
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onFailedJump(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.FAILED_JUMP, x, y, areaId);
    }

    /**
     * 记录一次被敌人击败
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onEnemyHit(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.ENEMY_HIT, x, y, areaId);
    }

    /**
     * 记录一次踩死敌人
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onEnemyKilled(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.ENEMY_KILLED, x, y, areaId);
    }

    /**
     * 记录一次破坏可破坏方块
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onBreakableBlockBroken(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.BREAKABLE_BLOCK_BROKEN, x, y, areaId);
    }

    /**
     * 记录一次死亡
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onDeath(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.DEATH, x, y, areaId);
        // 同一区域多次死亡属于困难强信号
        String key = buildAreaKey(levelIndex, areaId);
        areaDeathCounts.put(key, areaDeathCounts.getOrDefault(key, 0) + 1);
    }

    /**
     * 记录一次主动重开本关
     *
     * @param levelIndex 当前关卡索引
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    public synchronized void onRestart(int levelIndex, int x, int y, int areaId) {
        currentLevelIndex = levelIndex;
        currentAreaId = areaId;
        currentX = x;
        currentY = y;
        recordAction(ActionType.RESTART, x, y, areaId);
        // 同一区域多次重开也常表示卡关
        String key = buildAreaKey(levelIndex, areaId);
        areaRestartCounts.put(key, areaRestartCounts.getOrDefault(key, 0) + 1);
    }

    /**
     * 构建当前行为快照
     *
     * @return 当前滚动窗口内的统计结果
     */
    public synchronized BehaviorSnapshot buildSnapshot() {
        // 快照只基于最近窗口内的数据
        trimExpiredSamples();

        // 用窗口内横坐标跨度估算最近的推进距离
        int minX = currentX;
        int maxX = currentX;
        long firstFrameInWindow = currentFrame;
        if (!recentPositions.isEmpty()) {
            firstFrameInWindow = recentPositions.peekFirst().getFrame();
        }
        for (PositionSample sample : recentPositions) {
            minX = Math.min(minX, sample.getX());
            maxX = Math.max(maxX, sample.getX());
        }

        int progressDistanceRecent = maxX - minX;
        // 推进距离过低时认为玩家可能在原地徘徊
        long stuckFrames = progressDistanceRecent <= STUCK_PROGRESS_THRESHOLD
                ? Math.max(0, currentFrame - firstFrameInWindow)
                : 0;

        // 行为事件按类型计数形成特征向量
        int jumpCountRecent = countActions(ActionType.JUMP);
        int failedJumpCountRecent = countActions(ActionType.FAILED_JUMP);
        int enemyHitCountRecent = countActions(ActionType.ENEMY_HIT);
        int enemyKillCountRecent = countActions(ActionType.ENEMY_KILLED);
        int breakableBlockHitCountRecent = countActions(ActionType.BREAKABLE_BLOCK_BROKEN);
        int deathCountRecent = countActions(ActionType.DEATH);
        int restartCountRecent = countActions(ActionType.RESTART);
        int sameAreaDeathCount = areaDeathCounts.getOrDefault(buildAreaKey(currentLevelIndex, currentAreaId), 0);
        int sameAreaRestartCount = areaRestartCounts.getOrDefault(buildAreaKey(currentLevelIndex, currentAreaId), 0);
        int oscillationCountRecent = directionChangeFrames.size();

        return new BehaviorSnapshot(
                currentLevelIndex,
                currentX,
                currentY,
                currentAreaId,
                progressDistanceRecent,
                stuckFrames,
                jumpCountRecent,
                failedJumpCountRecent,
                enemyHitCountRecent,
                enemyKillCountRecent,
                breakableBlockHitCountRecent,
                deathCountRecent,
                restartCountRecent,
                sameAreaDeathCount,
                sameAreaRestartCount,
                oscillationCountRecent
        );
    }

    /**
     * 获取当前帧号
     *
     * @return 当前帧号
     */
    public synchronized long getCurrentFrame() {
        return currentFrame;
    }

    /**
     * 清空滚动窗口和方向变化统计
     */
    private void clearRollingState() {
        recentPositions.clear();
        recentActions.clear();
        directionChangeFrames.clear();
        lastX = null;
        lastDirection = 0;
    }

    /**
     * 记录一次关键行为
     *
     * @param type 行为类型
     * @param x 触发时横坐标
     * @param y 触发时纵坐标
     * @param areaId 当前区域编号
     */
    private void recordAction(ActionType type, int x, int y, int areaId) {
        recentActions.addLast(new ActionSample(currentFrame, type, x, y, areaId));
        trimExpiredSamples();
    }

    /**
     * 更新最近窗口内的左右折返次数
     *
     * @param newX 当前帧横坐标
     */
    private void updateOscillation(int newX) {
        if (lastX == null) {
            lastX = newX;
            return;
        }
        int deltaX = newX - lastX;
        lastX = newX;
        // 位移过小不计入方向变化 避免站立抖动造成误判
        if (Math.abs(deltaX) < DIRECTION_CHANGE_MIN_DELTA) {
            return;
        }
        int direction = deltaX > 0 ? 1 : -1;
        if (lastDirection != 0 && direction != lastDirection) {
            // 在窗口内记录折返发生的帧号
            directionChangeFrames.addLast(currentFrame);
        }
        lastDirection = direction;
    }

    /**
     * 剪掉滚动窗口之外的历史样本
     */
    private void trimExpiredSamples() {
        long minFrame = Math.max(0, currentFrame - WINDOW_FRAMES);
        while (!recentPositions.isEmpty() && recentPositions.peekFirst().getFrame() < minFrame) {
            recentPositions.removeFirst();
        }
        while (!recentActions.isEmpty() && recentActions.peekFirst().getFrame() < minFrame) {
            recentActions.removeFirst();
        }
        while (!directionChangeFrames.isEmpty() && directionChangeFrames.peekFirst() < minFrame) {
            directionChangeFrames.removeFirst();
        }
    }

    /**
     * 统计最近窗口内指定类型的行为数量
     *
     * @param type 行为类型
     * @return 最近窗口内该行为发生次数
     */
    private int countActions(ActionType type) {
        int count = 0;
        for (ActionSample sample : recentActions) {
            if (sample.getType() == type) {
                count++;
            }
        }
        return count;
    }

    /**
     * 拼接区域统计键
     *
     * @param levelIndex 关卡索引
     * @param areaId 区域编号
     * @return 区域统计键
     */
    private String buildAreaKey(int levelIndex, int areaId) {
        return levelIndex + ":" + areaId;
    }
}
