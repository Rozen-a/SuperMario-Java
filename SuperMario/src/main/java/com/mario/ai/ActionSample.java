package com.mario.ai;

/**
 * 行为采样点
 */
public class ActionSample {
    private final long frame;
    private final ActionType type;
    private final int x;
    private final int y;
    private final int areaId;

    /**
     * 创建行为采样点
     *
     * @param frame 帧号
     * @param type 行为类型
     * @param x 横坐标
     * @param y 纵坐标
     * @param areaId 所在区域编号
     */
    public ActionSample(long frame, ActionType type, int x, int y, int areaId) {
        this.frame = frame;
        this.type = type;
        this.x = x;
        this.y = y;
        this.areaId = areaId;
    }

    public long getFrame() {
        return frame;
    }

    public ActionType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAreaId() {
        return areaId;
    }
}
