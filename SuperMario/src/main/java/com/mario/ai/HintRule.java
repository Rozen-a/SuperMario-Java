package com.mario.ai;

/**
 * 单条提示规则
 *
 * areaId 用于提示系统的区域标识与去重展示
 */
public class HintRule {
    private final int levelIndex;   // 关卡索引
    private final int areaId;       // 区域编号
    private final int startX;     // 区域起始 x
    private final int endX;       // 区域结束 x
    private final HintType hintType;  // 提示类型
    private final String hintText;  // 提示文本

    /**
     * 创建提示规则
     *
     * @param levelIndex 关卡索引
     * @param areaId 区域编号
     * @param startX 区域起始 x
     * @param endX 区域结束 x
     * @param hintType 提示类型
     * @param hintText 提示文本
     */
    public HintRule(int levelIndex, int areaId, int startX, int endX, HintType hintType, String hintText) {
        this.levelIndex = levelIndex;
        this.areaId = areaId;
        this.startX = startX;
        this.endX = endX;
        this.hintType = hintType;
        this.hintText = hintText;
    }

    /**
     * 判断规则是否命中当前位置和提示类型
     *
     * @param targetLevelIndex 当前关卡索引
     * @param targetX 当前横坐标
     * @param targetType 目标提示类型
     * @return true 表示命中
     */
    public boolean matches(int targetLevelIndex, int targetX, HintType targetType) {
        // 匹配以关卡 横坐标区间和提示类型为主
        return levelIndex == targetLevelIndex
                && hintType == targetType
                && targetX >= startX
                && targetX <= endX;
    }

    /**
     * 判断规则是否命中当前位置
     *
     * @param targetLevelIndex 当前关卡索引
     * @param targetX 当前横坐标
     * @return true 表示命中
     */
    public boolean matchesArea(int targetLevelIndex, int targetX) {
        // 用于在找不到完全匹配类型时进行区域回退
        return levelIndex == targetLevelIndex
                && targetX >= startX
                && targetX <= endX;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getAreaId() {
        return areaId;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public HintType getHintType() {
        return hintType;
    }

    public String getHintText() {
        return hintText;
    }
}
