package com.mario.level;

import com.mario.entity.type.ObstacleType;
import com.mario.util.Background;

/**
 * 第二关地图
 */
public class Level2MapLoader implements LevelMapLoader {
    /**
     * 加载第二关地图障碍物
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
        // 山
        background.addObstacle(270, 420, ObstacleType.BREAKABLE_BLOCK);
        for (int i = 0; i < 3; i++) {
            background.addObstacle(300 + i * 30, 420, ObstacleType.UNBREAKABLE_BLOCK);
        }
        background.addObstacle(390, 420, ObstacleType.BREAKABLE_BLOCK);
        background.addObstacle(300, 390, ObstacleType.BREAKABLE_BLOCK);
        background.addObstacle(330, 390, ObstacleType.UNBREAKABLE_BLOCK);
        background.addObstacle(360, 390, ObstacleType.BREAKABLE_BLOCK);
        background.addObstacle(330, 360, ObstacleType.BREAKABLE_BLOCK);

        // 浮空可破坏
        background.addObstacle(270, 330, ObstacleType.BREAKABLE_BLOCK);

        // 浮空间断不可破坏
        background.addObstacle(390, 300, ObstacleType.UNBREAKABLE_BLOCK);
        background.addObstacle(450, 300, ObstacleType.UNBREAKABLE_BLOCK);
        background.addObstacle(510, 300, ObstacleType.UNBREAKABLE_BLOCK);
        background.addObstacle(570, 300, ObstacleType.UNBREAKABLE_BLOCK);

        // 斜坡
        for (int i = 0; i < 2; i++) {
            for (int j = 2 - i; j > 0; j--) {
                background.addObstacle(800 + (i + j - 1) * 30, 420 - i * 30, ObstacleType.UNBREAKABLE_BLOCK);
            }
        }

        /* 水管 */
        // 左
        background.addObstacle(90, 390, ObstacleType.PIPE1);
        background.addObstacle(120, 390, ObstacleType.PIPE2);
        for (int i = 0; i < 6; i++) {
            background.addObstacle(92, 420 + i * 30, ObstacleType.PIPE3);
            background.addObstacle(120, 420 + i * 30, ObstacleType.PIPE4);
        }

        // 右
        background.addObstacle(660, 360, ObstacleType.PIPE1);
        background.addObstacle(690, 360, ObstacleType.PIPE2);
        for (int i = 0; i < 7; i++) {
            background.addObstacle(662, 390 + i * 30, ObstacleType.PIPE3);
            background.addObstacle(690, 390 + i * 30, ObstacleType.PIPE4);
        }
    }
}
