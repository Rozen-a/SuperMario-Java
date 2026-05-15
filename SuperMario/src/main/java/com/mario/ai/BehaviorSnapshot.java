package com.mario.ai;

/**
 * 当前行为特征快照
 *
 * 用于把跟踪器中的滚动统计结果传递给困难分析器
 */
public class BehaviorSnapshot {
    private final int levelIndex;
    private final int currentX;
    private final int currentY;
    private final int currentAreaId;
    private final int progressDistanceRecent;
    private final long stuckFrames;
    private final int jumpCountRecent;
    private final int failedJumpCountRecent;
    private final int enemyHitCountRecent;
    private final int enemyKillCountRecent;
    private final int breakableBlockHitCountRecent;
    private final int deathCountRecent;
    private final int restartCountRecent;
    private final int sameAreaDeathCount;
    private final int sameAreaRestartCount;
    private final int oscillationCountRecent;

    /**
     * 创建行为特征快照
     *
     * @param levelIndex 关卡索引
     * @param currentX 当前横坐标
     * @param currentY 当前纵坐标
     * @param currentAreaId 当前区域编号
     * @param progressDistanceRecent 最近窗口内前进距离
     * @param stuckFrames 最近窗口内停留帧数
     * @param jumpCountRecent 最近窗口内成功起跳次数
     * @param failedJumpCountRecent 最近窗口内跳跃受阻次数
     * @param enemyHitCountRecent 最近窗口内被敌人击中次数
     * @param enemyKillCountRecent 最近窗口内击败敌人次数
     * @param breakableBlockHitCountRecent 最近窗口内破坏可破坏方块次数
     * @param deathCountRecent 最近窗口内死亡次数
     * @param restartCountRecent 最近窗口内主动重开次数
     * @param sameAreaDeathCount 当前区域累计死亡次数
     * @param sameAreaRestartCount 当前区域累计重开次数
     * @param oscillationCountRecent 最近窗口内折返次数
     */
    public BehaviorSnapshot(int levelIndex, int currentX, int currentY, int currentAreaId,
                            int progressDistanceRecent, long stuckFrames, int jumpCountRecent,
                            int failedJumpCountRecent, int enemyHitCountRecent, int enemyKillCountRecent,
                            int breakableBlockHitCountRecent, int deathCountRecent, int restartCountRecent,
                            int sameAreaDeathCount, int sameAreaRestartCount, int oscillationCountRecent) {
        this.levelIndex = levelIndex;
        this.currentX = currentX;
        this.currentY = currentY;
        this.currentAreaId = currentAreaId;
        this.progressDistanceRecent = progressDistanceRecent;
        this.stuckFrames = stuckFrames;
        this.jumpCountRecent = jumpCountRecent;
        this.failedJumpCountRecent = failedJumpCountRecent;
        this.enemyHitCountRecent = enemyHitCountRecent;
        this.enemyKillCountRecent = enemyKillCountRecent;
        this.breakableBlockHitCountRecent = breakableBlockHitCountRecent;
        this.deathCountRecent = deathCountRecent;
        this.restartCountRecent = restartCountRecent;
        this.sameAreaDeathCount = sameAreaDeathCount;
        this.sameAreaRestartCount = sameAreaRestartCount;
        this.oscillationCountRecent = oscillationCountRecent;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getCurrentAreaId() {
        return currentAreaId;
    }

    public int getProgressDistanceRecent() {
        return progressDistanceRecent;
    }

    public long getStuckFrames() {
        return stuckFrames;
    }

    public int getJumpCountRecent() {
        return jumpCountRecent;
    }

    public int getFailedJumpCountRecent() {
        return failedJumpCountRecent;
    }

    public int getEnemyHitCountRecent() {
        return enemyHitCountRecent;
    }

    public int getEnemyKillCountRecent() {
        return enemyKillCountRecent;
    }

    public int getBreakableBlockHitCountRecent() {
        return breakableBlockHitCountRecent;
    }

    public int getDeathCountRecent() {
        return deathCountRecent;
    }

    public int getRestartCountRecent() {
        return restartCountRecent;
    }

    public int getSameAreaDeathCount() {
        return sameAreaDeathCount;
    }

    public int getSameAreaRestartCount() {
        return sameAreaRestartCount;
    }

    public int getOscillationCountRecent() {
        return oscillationCountRecent;
    }
}
