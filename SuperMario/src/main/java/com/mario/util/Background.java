package com.mario.util;

import com.mario.entity.Obstacle;
import com.mario.entity.ObstacleType;
import com.mario.level.Level1MapLoader;
import com.mario.level.Level2MapLoader;
import com.mario.level.Level3MapLoader;
import com.mario.level.LevelMapLoader;

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

    /**
     * 无参构造
     */
    public Background() {
    }

    /**
     * 创建场景并加载对应关卡地图
     *
     * @param sort 场景序号
     * @param flag 是否为最后场景
     */
    public Background(int sort, boolean flag) {
        this.sort = sort;
        this.flag = flag;

        // 最后一个场景时切换为第二个背景
        if (flag) {
            bgImage = StaticValue.background1;
        } else {
            bgImage = StaticValue.background2;
        }

        loadLevelMap();
    }

    /**
     * 按场景序号加载关卡地图
     */
    private void loadLevelMap() {
        LevelMapLoader loader;
        switch (sort) {
            case 1:
                loader = new Level1MapLoader();
                break;
            case 2:
                loader = new Level2MapLoader();
                break;
            case 3:
                loader = new Level3MapLoader();
                break;
            default:
                return;
        }
        loader.load(this);
    }

    /**
     * 添加障碍物到当前场景
     *
     * @param x 障碍物 x 坐标
     * @param y 障碍物 y 坐标
     * @param type 障碍物类型
     */
    public void addObstacle(int x, int y, ObstacleType type) {
        obstacles.add(new Obstacle(x, y, type, this));
    }

    /**
     * 获取当前场景背景图
     */
    public BufferedImage getBgImage() {
        return bgImage;
    }

    /**
     * 设置当前场景背景图
     */
    public void setBgImage(BufferedImage bgImage) {
        this.bgImage = bgImage;
    }

    /**
     * 获取场景序号
     */
    public int getSort() {
        return sort;
    }

    /**
     * 设置场景序号
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * 是否为最后场景
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * 设置是否为最后场景
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 获取当前场景障碍物列表
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * 设置当前场景障碍物列表
     */
    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }
}
