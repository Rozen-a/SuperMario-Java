package com.mario.ai;

/**
 * 位置采样点
 */
public class PositionSample {
    private final long frame;
    private final int x;
    private final int y;

    /**
     * 创建位置采样点
     *
     * @param frame 帧号
     * @param x 横坐标
     * @param y 纵坐标
     */
    public PositionSample(long frame, int x, int y) {
        this.frame = frame;
        this.x = x;
        this.y = y;
    }

    public long getFrame() {
        return frame;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
