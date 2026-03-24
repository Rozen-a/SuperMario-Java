package com.mario.entity.type;

/**
 * 障碍物类型
 */
public enum ObstacleType {
    BREAKABLE_BLOCK(0),
    UNBREAKABLE_BLOCK(1),
    SOIL_UP(2),
    SOIL_BASE(3),
    PIPE1(4),
    PIPE2(5),
    PIPE3(6),
    PIPE4(7);

    private final int spriteIndex;

    ObstacleType(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
