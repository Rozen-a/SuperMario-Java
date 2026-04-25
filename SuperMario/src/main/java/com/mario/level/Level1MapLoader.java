package com.mario.level;

import com.mario.enums.ObstacleType;
import com.mario.enums.ManEatingFlowerSpriteType;
import com.mario.enums.MushroomSpriteType;
import com.mario.entity.scene.Background;

/**
 * 第一关地图
 */
public class Level1MapLoader implements LevelMapLoader {
    /**
     * 加载第一关地图障碍物
     *
     * @param background 目标背景
     */
    @Override
    public void load(Background background) {
        /* 地面 */
        // 上层地面
        for (int i = 0; i < 30; i++) {
            background.addObstacle(i * 30, 450, ObstacleType.SOIL_UP);
        }
        // 下层地面
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 30; j++) {
                background.addObstacle(j * 30, 570 - i * 30, ObstacleType.SOIL_BASE);
            }
        }

        /* 方块 */
        // unbreakable*2
        for (int i = 0; i < 2; i++) {
            background.addObstacle(150 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }

        background.addObstacle(210 , 390, ObstacleType.UNBREAKABLE_BLOCK);

        // unbreakable*2
        for (int i = 0; i < 2; i++) {
            background.addObstacle(240 + i * 30, 360, ObstacleType.UNBREAKABLE_BLOCK);
        }

        // unbreakable*1
        background.addObstacle(330, 360, ObstacleType.UNBREAKABLE_BLOCK);

        // unbreakable*1
        background.addObstacle(360, 330, ObstacleType.UNBREAKABLE_BLOCK);

        // unbreakable*1
        background.addObstacle(450, 390, ObstacleType.UNBREAKABLE_BLOCK);

        // unbreakable*1
        background.addObstacle(600, 390, ObstacleType.UNBREAKABLE_BLOCK);

        // breakable*2 + unbreakable*3 + breakable*1 + unbreakable*3 + breakable*1
        for (int i = 0; i < 2; i++) {
            background.addObstacle(420 + i * 30, 300, ObstacleType.BREAKABLE_BLOCK);
        }
        for (int i = 0; i < 3; i++) {
            background.addObstacle(480 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }
        background.addObstacle(570, 300, ObstacleType.BREAKABLE_BLOCK);
        for (int i = 0; i < 3; i++) {
            background.addObstacle(600 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }
        background.addObstacle(690, 300, ObstacleType.BREAKABLE_BLOCK);

        // unbreakable*4
        for (int i = 0; i < 4; i++) {
            background.addObstacle(540 + i * 30, 240, ObstacleType.UNBREAKABLE_BLOCK);
        }

        /* 水管 */
        background.addObstacle(720, 390, ObstacleType.PIPE1);
        background.addObstacle(750, 390, ObstacleType.PIPE2);
        for (int i = 0; i < 6; i++) {
            background.addObstacle(722, 420 + i * 30, ObstacleType.PIPE3);
            background.addObstacle(750, 420 + i * 30, ObstacleType.PIPE4);
        }

        /* 敌人 */
        background.addManEatingFlower(735, 390, true, ManEatingFlowerSpriteType.FRAME_1);
        background.addMushroom(180, 420, true, 140, 420, MushroomSpriteType.STAND);
        background.addMushroom(550, 420, true, 500, 600, MushroomSpriteType.STAND);
    }
}
