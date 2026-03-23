package com.mario.Entity;

/**
 * 障碍物类型
 */
public enum ObstacleType {
    BREAKABLE_BLOCK(0),
    UNBREAKABLE_BLOCK(1),
    SOIL_UP(2),
    SOIL_BASE(3),
    FLAG(4),
    PIPE1(5),
    PIPE2(6),
    PIPE3(7),
    PIPE4(8);

    private final int spriteIndex;

    ObstacleType(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
