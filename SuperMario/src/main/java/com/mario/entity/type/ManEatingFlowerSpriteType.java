package com.mario.entity.type;

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

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
