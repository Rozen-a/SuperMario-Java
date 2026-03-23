package com.mario.entity;

/**
 * 食人花图片类型
 */
public enum ManEatingFlowerSpriteType {
    FRAME_1(0),
    FRAME_2(1);

    private final int spriteIndex;

    ManEatingFlowerSpriteType(int spriteIndex) {
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
