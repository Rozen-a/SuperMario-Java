package com.mario.entity.creature;

import com.mario.util.Background;

/**
 * 蘑菇敌人
 */
public class Mushroom extends Enemy {
    public Mushroom() {
    }

    public Mushroom(int x, int y, boolean toRight, Background background) {
        super(x, y, toRight, background);
    }
}
