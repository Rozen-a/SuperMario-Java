package com.mario.entity.creature;

import com.mario.entity.type.ManEatingFlowerSpriteType;
import com.mario.util.Background;
import com.mario.util.StaticValue;

/**
 * 食人花敌人
 */
public class ManEatingFlower extends Enemy {
    public ManEatingFlower() {
    }

    /**
     * 创建食人花敌人对象
     *
     * @param x 食人花敌人 x 坐标
     * @param y 食人花敌人 y 坐标
     * @param toRight 食人花敌人运动方向
     * @param background 食人花敌人所在背景
     * @param type 食人花敌人图片类型
     */
    public ManEatingFlower(int x, int y, boolean toRight, Background background, ManEatingFlowerSpriteType type) {
        super(x, y, toRight, background);
        setShow(StaticValue.man_eating_flower.get(type.getSpriteIndex()));
    }
}
