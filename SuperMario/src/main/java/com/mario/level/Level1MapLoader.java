package com.mario.level;

import com.mario.entity.ObstacleType;
import com.mario.util.Background;

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

        /* 浮空岛 */
        // unbreakable*2
        for (int i = 0; i < 2; i++) {
            background.addObstacle(150 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }

        // breakable*2 + unbreakable*3 + breakable*1 + unbreakable*3 + breakable*1
        for (int i = 0; i < 2; i++) {
            background.addObstacle(330 + i * 30, 300, ObstacleType.BREAKABLE_BLOCK);
        }
        for (int i = 0; i < 3; i++) {
            background.addObstacle(390 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }
        background.addObstacle(480, 300, ObstacleType.BREAKABLE_BLOCK);
        for (int i = 0; i < 3; i++) {
            background.addObstacle(510 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK);
        }
        background.addObstacle(600, 300, ObstacleType.BREAKABLE_BLOCK);

        // unbreakable*4
        for (int i = 0; i < 4; i++) {
            background.addObstacle(450 + i * 30, 240, ObstacleType.UNBREAKABLE_BLOCK);
        }

        /* 水管 */
        background.addObstacle(630, 390, ObstacleType.PIPE1);
        background.addObstacle(660, 390, ObstacleType.PIPE2);
        for (int i = 0; i < 6; i++) {
            background.addObstacle(632, 420 + i * 30, ObstacleType.PIPE3);
            background.addObstacle(660, 420 + i * 30, ObstacleType.PIPE4);
        }
    }
}
