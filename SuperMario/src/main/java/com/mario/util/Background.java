package com.mario.util;

import com.mario.Entity.Obstacle;
import com.mario.Entity.ObstacleType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 背景类
 */
public class Background {
    private BufferedImage bgImage = null;  // 当前场景图片
    private int sort;  // 当前场景序号
    private boolean flag;  // 判断是否到达最后一个场景
    private List<Obstacle> obstacles = new ArrayList<>();  // 障碍物列表

    /* Constructor */
    public Background() {
    }

    public Background(int sort, boolean flag) {
        this.sort = sort;
        this.flag = flag;

        // 最后一个场景时切换为第二个背景
        if (flag) {
            bgImage = StaticValue.background1;
        } else {
            bgImage = StaticValue.background2;
        }

        if (sort == 1) {  // 绘制第一关地图
            /* 地面 */
            // 上层地面
            for (int i = 0; i < 30; i++) {
                obstacles.add(new Obstacle(i * 30, 450, ObstacleType.SOIL_UP, this));
            }
            // 下层地面
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 30; j++) {
                    obstacles.add(new Obstacle(j * 30, 570 - i * 30, ObstacleType.SOIL_BASE, this));
                }   
            }

            /* 浮空岛 */
            // unbreakable*2
            for (int i = 0; i < 2; i++) {
                obstacles.add(new Obstacle(150 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK, this));
            }

            // breakable*2 + unbreakable*3 + breakable*1 + unbreakable*3 + breakable*1
            for (int i = 0; i < 2; i++) {
                obstacles.add(new Obstacle(330 + i * 30, 300, ObstacleType.BREAKABLE_BLOCK, this));
            }
            for (int i = 0; i < 3; i++) {
                obstacles.add(new Obstacle(390 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK, this));
            }
            obstacles.add(new Obstacle(480, 300, ObstacleType.BREAKABLE_BLOCK, this));
            for (int i = 0; i < 3; i++) {
                obstacles.add(new Obstacle(510 + i * 30, 300, ObstacleType.UNBREAKABLE_BLOCK, this));
            }
            obstacles.add(new Obstacle(600, 300, ObstacleType.BREAKABLE_BLOCK, this));

            // unbreakable*4
            for (int i = 0; i < 4; i++) {
                obstacles.add(new Obstacle(450 + i * 30, 240, ObstacleType.UNBREAKABLE_BLOCK, this));
            }

            /* 水管 */
            obstacles.add(new Obstacle(630, 390, ObstacleType.PIPE1, this));
            obstacles.add(new Obstacle(660, 390, ObstacleType.PIPE2, this));
            for (int i = 0; i < 6; i++) {
                obstacles.add(new Obstacle(632, 420 + i * 30, ObstacleType.PIPE3, this));
                obstacles.add(new Obstacle(660, 420 + i * 30, ObstacleType.PIPE4, this));
            }

        }
    }

    /* Getter and Setter */
    public BufferedImage getBgImage() {
        return bgImage;
    }

    public void setBgImage(BufferedImage bgImage) {
        this.bgImage = bgImage;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }
}
