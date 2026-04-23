package com.mario.entity.scene;

import com.mario.entity.creature.Enemy;
import com.mario.entity.creature.ManEatingFlower;
import com.mario.entity.creature.Mushroom;
import com.mario.enums.ManEatingFlowerSpriteType;
import com.mario.enums.MushroomSpriteType;
import com.mario.enums.ObstacleType;
import com.mario.level.Level1MapLoader;
import com.mario.level.Level2MapLoader;
import com.mario.level.Level3MapLoader;
import com.mario.level.LevelMapLoader;
import com.mario.constant.StaticValue;

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
    private List<Enemy> enemyList = new ArrayList<>();  // 敌人列表
    private Tower tower = null;
    private Flagpole flagpole = null;
    private Flag sceneFlag = null;

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
            bgImage = StaticValue.background2;
        } else {
            bgImage = StaticValue.background1;
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
     * 创建并添加食人花敌人
     *
     * @param x 食人花 x 坐标
     * @param y 食人花 y 坐标
     * @param toRight 食人花朝向
     * @param type 食人花图片类型
     */
    public void addManEatingFlower(int x, int y, boolean toRight, ManEatingFlowerSpriteType type) {
        enemyList.add(new ManEatingFlower(x, y, toRight, this, type));
    }

    /**
     * 创建并添加蘑菇敌人
     *
     * @param x 蘑菇 x 坐标
     * @param y 蘑菇 y 坐标
     * @param toRight 蘑菇朝向
     * @param leftLimitX 蘑菇运动左边界
     * @param rightLimitX 蘑菇运动右边界
     * @param type 蘑菇图片类型
     */
    public void addMushroom(int x, int y, boolean toRight, int leftLimitX, int rightLimitX, MushroomSpriteType type) {
        enemyList.add(new Mushroom(x, y, toRight, this, leftLimitX, rightLimitX, type));
    }

    /**
     * 添加城堡到当前场景
     *
     * @param x 城堡 x 坐标
     * @param y 城堡 y 坐标
     */
    public void addTower(int x, int y) {
        this.tower = new Tower(x, y, this);
    }

    /**
     * 添加旗杆到当前场景
     *
     * @param x 旗杆 x 坐标
     * @param y 旗杆 y 坐标
     */
    public void addFlagpole(int x, int y) {
        this.flagpole = new Flagpole(x, y, this);
    }

    /**
     * 添加旗子到当前场景
     *
     * @param x 旗子 x 坐标
     * @param y 旗子 y 坐标
     */
    public void addFlag(int x, int y) {
        this.sceneFlag = new Flag(x, y, this);
    }

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

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Flagpole getFlagpole() {
        return flagpole;
    }

    public void setFlagpole(Flagpole flagpole) {
        this.flagpole = flagpole;
    }

    public Flag getFlag() {
        return sceneFlag;
    }

    public void setFlag(Flag flag) {
        this.sceneFlag = flag;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public void setEnemyList(List<Enemy> enemyList) {
        this.enemyList = enemyList;
    }

    public Flag getSceneFlag() {
        return sceneFlag;
    }

    public void setSceneFlag(Flag sceneFlag) {
        this.sceneFlag = sceneFlag;
    }
}
