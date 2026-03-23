package com.mario.entity;

/**
 * 蘑菇敌人图片类型
 */
public enum MushroomSpriteType {
    SQUISHED(0),
    STAND(1),
    WALK_1(2),
    WALK_2(3);

    private final int spriteIndex;

    MushroomSpriteType(int spriteIndex) {
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
