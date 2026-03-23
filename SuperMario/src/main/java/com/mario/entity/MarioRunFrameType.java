package com.mario.entity;

/**
 * 马里奥跑动帧类型
 * 
 * 往左和往右共用一套帧编号
 */
public enum MarioRunFrameType {
    FRAME_1(0),
    FRAME_2(1);

    private final int spriteIndex;

    MarioRunFrameType(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    /**
     * 获取在图片列表中的索引
     *
     * @return 图片索引
     */
    public int getSpriteIndex() {
        return spriteIndex;
    }
}
