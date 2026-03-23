package com.mario.level;

import com.mario.entity.scene.ObstacleType;
import com.mario.util.Background;

/**
 * 第三关地图
 */
public class Level3MapLoader implements LevelMapLoader {
    /**
     * 加载第三关地图障碍物
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
        // 斜坡1
        for (int i = 0; i < 2; i++) {
            for (int j = 2 - i; j > 0; j--) {
                background.addObstacle(120 + (i + j - 1) * 30, 420 - i * 30, ObstacleType.UNBREAKABLE_BLOCK);
            }
        }

        // 斜坡2
        for (int i = 0; i < 5; i++) {
            for (int j = 5 - i; j > 0; j--) {
                background.addObstacle(390 + (i + j - 1) * 30, 420 - i * 30, ObstacleType.UNBREAKABLE_BLOCK);
            }
        }

        /* 旗杆 */
        background.addFlagpole(600, 270);
        // 添加旗子到旗杆上
        background.addObstacle(615, 284, ObstacleType.FLAG);

        /* 城堡 */
        background.addTower(700, 300);
    }
}
