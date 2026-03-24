package com.mario.entity.creature;

import com.mario.util.Background;

/**
 * 食人花敌人
 */
public class ManEatingFlower extends Enemy {
    public ManEatingFlower() {
    }

    public ManEatingFlower(int x, int y, boolean toRight, Background background) {
        super(x, y, toRight, background);
    }
}
