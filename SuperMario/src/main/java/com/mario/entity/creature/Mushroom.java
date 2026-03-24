package com.mario.entity.creature;

import com.mario.entity.type.MushroomSpriteType;
import com.mario.util.Background;
import com.mario.util.StaticValue;

/**
 * 蘑菇敌人
 */
public class Mushroom extends Enemy {
    public Mushroom() {
    }

    /**
     * 创建蘑菇敌人对象
     *
     * @param x 蘑菇敌人 x 坐标
     * @param y 蘑菇敌人 y 坐标
     * @param toRight 蘑菇敌人运动方向
     * @param background 蘑菇敌人所在背景
     * @param type 蘑菇敌人图片类型
     */
    public Mushroom(int x, int y, boolean toRight, Background background, MushroomSpriteType type) {
        super(x, y, toRight, background);
        setShow(StaticValue.mushroom.get(type.getSpriteIndex()));
    }
}
